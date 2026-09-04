package hn.gob.ine.listener.service;

import hn.gob.ine.listener.dao.ControlCorteDAO;
import hn.gob.ine.listener.dao.DestinoMonitoreoDAO;
import hn.gob.ine.listener.dao.OrigenCnpvDAO;
import hn.gob.ine.listener.model.IndicadoresControlNacional;
import hn.gob.ine.listener.worker.DepartamentoWorker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CorteService {

    private ControlCorteDAO controlDAO;
    private OrigenCnpvDAO origenDAO;
    private DestinoMonitoreoDAO destinoDAO;

    public CorteService() {
        this.controlDAO = new ControlCorteDAO();
        this.origenDAO = new OrigenCnpvDAO();
        this.destinoDAO = new DestinoMonitoreoDAO();
    }

    public void ejecutarCorte(int idConfigCorte) {

        int idEjecucion = 0;

        try {
            idEjecucion = controlDAO.crearEjecucion(idConfigCorte);

            System.out.println("========================================");
            System.out.println("Ejecucion creada con ID: " + idEjecucion);
            System.out.println("Iniciando corte con 18 hilos...");
            System.out.println("========================================");

            ExecutorService executor = Executors.newFixedThreadPool(18);

            for (int i = 1; i <= 18; i++) {
                String depto = String.format("%02d", i);

                DepartamentoWorker worker = new DepartamentoWorker(idEjecucion, depto);

                executor.submit(worker);
            }

            executor.shutdown();

            boolean termino = executor.awaitTermination(2, TimeUnit.HOURS);

            if (termino) {
                try {
                    IndicadoresControlNacional indicadores = origenDAO.calcularIndicadoresControlNacional();
                    destinoDAO.refrescarIndicadoresControlNacional(indicadores);
                    System.out.println("Indicadores de control nacional actualizados -> "
                            + "estructuras: " + indicadores.getAvanceEstructurasPct() + "%, "
                            + "ocupadas presentes: " + indicadores.getOcupadasPresentesPct() + "%, "
                            + "ocupadas ausentes: " + indicadores.getOcupadasAusentesPct() + "%, "
                            + "desocupadas: " + indicadores.getDesocupadasPct() + "%");
                } catch (Exception e) {
                    System.err.println("Error calculando indicadores de control nacional:");
                    e.printStackTrace();
                }

                controlDAO.finalizarEjecucion(idEjecucion, "FINALIZADO");
                System.out.println("Corte finalizado correctamente.");
            } else {
                controlDAO.finalizarEjecucion(idEjecucion, "FINALIZADO_CON_TIMEOUT");
                System.err.println("El corte no termino dentro del tiempo esperado.");
            }

        } catch (Exception e) {
            System.err.println("Error general ejecutando corte:");

            if (idEjecucion > 0) {
                try {
                    controlDAO.finalizarEjecucion(idEjecucion, "ERROR_GENERAL");
                } catch (Exception ex) {
                    System.err.println("No se pudo actualizar la ejecucion con ERROR_GENERAL");
                    ex.printStackTrace();
                }
            }

            e.printStackTrace();
        }
    }
}