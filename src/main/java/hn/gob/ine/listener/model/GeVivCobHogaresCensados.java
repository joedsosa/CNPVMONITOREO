package hn.gob.ine.listener.model;

public class GeVivCobHogaresCensados {

    private String depto;
    private String mun;
    private int apoyoMunicipal;
    private int zona;
    private String sector;
    private String segmento;
    private String censista;
    private int cantidadCensadas;

    public GeVivCobHogaresCensados(String depto, String mun, int apoyoMunicipal, int zona, String sector, String segmento,
                                   String censista, int cantidadCensadas) {
        this.depto = depto;
        this.mun = mun;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.cantidadCensadas = cantidadCensadas;
    }

    public String getDepto() {
        return depto;
    }

    public String getMun() {
        return mun;
    }

    public int getApoyoMunicipal() {
        return apoyoMunicipal;
    }

    public int getZona() {
        return zona;
    }

    public String getSector() {
        return sector;
    }

    public String getSegmento() {
        return segmento;
    }

    public String getCensista() {
        return censista;
    }

    public int getCantidadCensadas() {
        return cantidadCensadas;
    }
}