package hn.gob.ine.listener.model;

public class GeVivCobDeptosCompletados {

    private String depto;
    private String completado;

    public GeVivCobDeptosCompletados(String depto, String completado) {
        this.depto = depto;
        this.completado = completado;
    }

    public String getDepto() { return depto; }
    public String getCompletado() { return completado; }
}