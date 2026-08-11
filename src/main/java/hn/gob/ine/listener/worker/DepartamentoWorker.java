package hn.gob.ine.listener.worker;

import hn.gob.ine.listener.dao.OrigenCnpvDAO;
import hn.gob.ine.listener.model.LlaveCensista;
import hn.gob.ine.listener.service.CensistaService;

import java.util.List;

public class DepartamentoWorker implements Runnable {

    private int idEjecucion;
    private String depto;

    private OrigenCnpvDAO origenDAO;
    private CensistaService censistaService;

    public DepartamentoWorker(int idEjecucion, String depto) {
        this.idEjecucion = idEjecucion;
        this.depto = depto;
        this.origenDAO = new OrigenCnpvDAO();
        this.censistaService = new CensistaService();
    }

    @Override
    public void run() {

        String nombreHilo = Thread.currentThread().getName();

        System.out.println("[" + nombreHilo + "] Iniciando departamento " + depto);

        try {
            List<LlaveCensista> censistas = origenDAO.obtenerCensistasPorDepartamento(depto);

            System.out.println("[" + nombreHilo + "] Departamento " + depto +
                    " tiene " + censistas.size() + " censistas/llaves.");

            for (LlaveCensista llave : censistas) {
                censistaService.procesarCensista(idEjecucion, llave);
            }

            System.out.println("[" + nombreHilo + "] Departamento " + depto + " finalizado.");

        } catch (Exception e) {
            System.err.println("[" + nombreHilo + "] Error general en departamento " + depto);
            e.printStackTrace();
        }
    }
}