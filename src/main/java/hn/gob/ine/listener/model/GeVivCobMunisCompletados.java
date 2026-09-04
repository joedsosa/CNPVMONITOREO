package hn.gob.ine.listener.model;

public class GeVivCobMunisCompletados {

    private String depto;
    private String muni;
    private String completado;

    public GeVivCobMunisCompletados(String depto, String muni, String completado) {
        this.depto = depto;
        this.muni = muni;
        this.completado = completado;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public String getCompletado() { return completado; }
}