package hn.gob.ine.listener.model;

public class CalidadHogHacinamiento {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int mayor3Hac;
    private int numPersonas;

    public CalidadHogHacinamiento(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int mayor3Hac, int numPersonas) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.mayor3Hac = mayor3Hac;
        this.numPersonas = numPersonas;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getMayor3Hac() { return mayor3Hac; }
    public int getNumPersonas() { return numPersonas; }
}