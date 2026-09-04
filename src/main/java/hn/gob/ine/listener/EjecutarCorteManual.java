package hn.gob.ine.listener;

import hn.gob.ine.listener.service.CorteService;

public class EjecutarCorteManual {
    public static void main(String[] args) {
        int idConfigCorte = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        System.out.println("Disparando corte manual, id_config_corte=" + idConfigCorte);
        new CorteService().ejecutarCorte(idConfigCorte);
        System.out.println("Corte manual finalizado.");
    }
}
