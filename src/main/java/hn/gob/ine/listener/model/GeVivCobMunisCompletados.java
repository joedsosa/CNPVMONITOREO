package hn.gob.ine.listener.model;

public class GeVivCobMunisCompletados {

    private String muni;
    private String completado;

    public GeVivCobMunisCompletados(String muni, String completado) {
        this.muni = muni;
        this.completado = completado;
    }

    public String getMuni() { return muni; }
    public String getCompletado() { return completado; }
}