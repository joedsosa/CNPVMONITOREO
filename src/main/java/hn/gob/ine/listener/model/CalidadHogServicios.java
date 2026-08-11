package hn.gob.ine.listener.model;

public class CalidadHogServicios {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int cantidad;
    private int refrigeradora;
    private int televisor;
    private int computadora;
    private int celular;
    private int internet;

    public CalidadHogServicios(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int cantidad, int refrigeradora,
            int televisor, int computadora, int celular, int internet) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.cantidad = cantidad;
        this.refrigeradora = refrigeradora;
        this.televisor = televisor;
        this.computadora = computadora;
        this.celular = celular;
        this.internet = internet;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getCantidad() { return cantidad; }
    public int getRefrigeradora() { return refrigeradora; }
    public int getTelevisor() { return televisor; }
    public int getComputadora() { return computadora; }
    public int getCelular() { return celular; }
    public int getInternet() { return internet; }
}