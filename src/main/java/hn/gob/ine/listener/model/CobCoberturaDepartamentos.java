package hn.gob.ine.listener.model;

public class CobCoberturaDepartamentos {

    private String depto;
    private String deptoName;
    private int realizado;
    private int porRealizar;

    public CobCoberturaDepartamentos(String depto, String deptoName, int realizado, int porRealizar) {
        this.depto = depto;
        this.deptoName = deptoName;
        this.realizado = realizado;
        this.porRealizar = porRealizar;
    }

    public String getDepto() { return depto; }
    public String getDeptoName() { return deptoName; }
    public int getRealizado() { return realizado; }
    public int getPorRealizar() { return porRealizar; }
}