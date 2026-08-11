package hn.gob.ine.listener.model;

public class EjecucionCorte {

    private int id;
    private int idConfigCorte;
    private String estado;

    public EjecucionCorte(int id, int idConfigCorte, String estado) {
        this.id = id;
        this.idConfigCorte = idConfigCorte;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public int getIdConfigCorte() {
        return idConfigCorte;
    }

    public String getEstado() {
        return estado;
    }
}