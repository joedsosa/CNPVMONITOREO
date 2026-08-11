package hn.gob.ine.listener.model;

public class CalidadTicAcceso {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int cantUnaTicMinimo;
    private int cantConCompu;
    private int cantConTablet;
    private int cantConCelular;
    private int cantConTelFijo;
    private int cantTieneInternet;
    private int poblacionTotal;

    public CalidadTicAcceso(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int cantUnaTicMinimo, int cantConCompu,
            int cantConTablet, int cantConCelular, int cantConTelFijo,
            int cantTieneInternet, int poblacionTotal) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.cantUnaTicMinimo = cantUnaTicMinimo;
        this.cantConCompu = cantConCompu;
        this.cantConTablet = cantConTablet;
        this.cantConCelular = cantConCelular;
        this.cantConTelFijo = cantConTelFijo;
        this.cantTieneInternet = cantTieneInternet;
        this.poblacionTotal = poblacionTotal;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getCantUnaTicMinimo() { return cantUnaTicMinimo; }
    public int getCantConCompu() { return cantConCompu; }
    public int getCantConTablet() { return cantConTablet; }
    public int getCantConCelular() { return cantConCelular; }
    public int getCantConTelFijo() { return cantConTelFijo; }
    public int getCantTieneInternet() { return cantTieneInternet; }
    public int getPoblacionTotal() { return poblacionTotal; }
}