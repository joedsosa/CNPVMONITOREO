package hn.gob.ine.listener.model;

import java.sql.Time;

public class CorteConfig {

    private int id;
    private String nombre;
    private boolean activo;
    private Time horaCorte;
    private String descripcion;

    public CorteConfig(int id, String nombre, boolean activo, Time horaCorte, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.activo = activo;
        this.horaCorte = horaCorte;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public Time getHoraCorte() {
        return horaCorte;
    }

    public String getDescripcion() {
        return descripcion;
    }
}