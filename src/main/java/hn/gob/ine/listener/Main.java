package hn.gob.ine.listener;

import hn.gob.ine.listener.config.DataSourceFactory;
import hn.gob.ine.listener.scheduler.SchedulerCortes;
import hn.gob.ine.listener.util.TeeOutputStream;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        configurarLogAArchivo();

        System.out.println("Listener Censo Monitoreo iniciado correctamente.");
        System.out.println("Modo produccion: scheduler activado, revisando config_cortes cada minuto.");

        final SchedulerCortes scheduler = new SchedulerCortes();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Señal de apagado recibida, deteniendo listener...");
            scheduler.detener();
            DataSourceFactory.cerrarTodo();
        }));

        scheduler.iniciar();
    }

    private static void configurarLogAArchivo() {

        try {
            File logDir = new File("logs");

            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            String nombreArchivo = "logs/listener-" + LocalDate.now() + ".log";
            FileOutputStream archivoLog = new FileOutputStream(nombreArchivo, true);

            System.setOut(new PrintStream(new TeeOutputStream(System.out, archivoLog), true));
            System.setErr(new PrintStream(new TeeOutputStream(System.err, archivoLog), true));

            System.out.println("Log de esta corrida tambien se esta guardando en: " + nombreArchivo);

        } catch (Exception e) {
            System.err.println("No se pudo configurar el log a archivo, se sigue solo por consola: " + e.getMessage());
        }
    }
}