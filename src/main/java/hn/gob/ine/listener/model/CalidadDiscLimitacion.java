package hn.gob.ine.listener.model;

public class CalidadDiscLimitacion {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int cantLimitacion;
    private int cantLimCaminar;
    private int cantLimHabla;
    private int cantLimVision;
    private int cantLimOir;
    private int cantLimAprender;
    private int cantLimBrazo;
    private int cantLimValerse;
    private int cantSinLimitacion;

    public CalidadDiscLimitacion(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int cantLimitacion, int cantLimCaminar,
            int cantLimHabla, int cantLimVision, int cantLimOir, int cantLimAprender,
            int cantLimBrazo, int cantLimValerse, int cantSinLimitacion) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.cantLimitacion = cantLimitacion;
        this.cantLimCaminar = cantLimCaminar;
        this.cantLimHabla = cantLimHabla;
        this.cantLimVision = cantLimVision;
        this.cantLimOir = cantLimOir;
        this.cantLimAprender = cantLimAprender;
        this.cantLimBrazo = cantLimBrazo;
        this.cantLimValerse = cantLimValerse;
        this.cantSinLimitacion = cantSinLimitacion;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getCantLimitacion() { return cantLimitacion; }
    public int getCantLimCaminar() { return cantLimCaminar; }
    public int getCantLimHabla() { return cantLimHabla; }
    public int getCantLimVision() { return cantLimVision; }
    public int getCantLimOir() { return cantLimOir; }
    public int getCantLimAprender() { return cantLimAprender; }
    public int getCantLimBrazo() { return cantLimBrazo; }
    public int getCantLimValerse() { return cantLimValerse; }
    public int getCantSinLimitacion() { return cantSinLimitacion; }
}