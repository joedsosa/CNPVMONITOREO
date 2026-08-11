package hn.gob.ine.listener.scheduler;

import hn.gob.ine.listener.dao.ConfigCorteDAO;
import hn.gob.ine.listener.dao.ControlCorteDAO;
import hn.gob.ine.listener.model.CorteConfig;
import hn.gob.ine.listener.service.CorteService;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerCortes {

    private ScheduledExecutorService scheduler;

    private ConfigCorteDAO configCorteDAO;
    private ControlCorteDAO controlCorteDAO;
    private CorteService corteService;

    public SchedulerCortes() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.configCorteDAO = new ConfigCorteDAO();
        this.controlCorteDAO = new ControlCorteDAO();
        this.corteService = new CorteService();
    }

    public void iniciar() {

        System.out.println("Scheduler de cortes iniciado.");
        System.out.println("Revisando configuracion cada 1 minuto...");

        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                revisarCortes();
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    private void revisarCortes() {

        try {
            List<CorteConfig> cortes = configCorteDAO.obtenerCortesActivos();

            LocalTime horaActual = LocalTime.now();

            int hora = horaActual.getHour();
            int minuto = horaActual.getMinute();

            for (CorteConfig corte : cortes) {

                Time horaCorteSql = corte.getHoraCorte();
                LocalTime horaCorte = horaCorteSql.toLocalTime();

                boolean mismaHora = hora == horaCorte.getHour();
                boolean mismoMinuto = minuto == horaCorte.getMinute();

                if (mismaHora && mismoMinuto) {

                    System.out.println("Hora de corte detectada: " + corte.getNombre());

                    boolean yaCorrio = controlCorteDAO.existeEjecucionHoy(corte.getId());

                    if (yaCorrio) {
                        System.out.println("El corte " + corte.getNombre() + " ya fue ejecutado hoy.");
                    } else {
                        System.out.println("Ejecutando corte: " + corte.getNombre());
                        corteService.ejecutarCorte(corte.getId());
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error revisando cortes configurados:");
            e.printStackTrace();
        }
    }

    public void detener() {
        if (scheduler != null) {
            scheduler.shutdown();
        }
    }
}