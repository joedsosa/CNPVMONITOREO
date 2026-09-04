package hn.gob.ine.listener.model;

public class CobArea {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private String sector;
    private String segmento;
    private String censista;

    private int areaUrbana;
    private int areaRural;

    public CobArea(String depto, String muni, int apoyoMunicipal, int zona, String sector, String segmento, String censista,
                   int areaUrbana, int areaRural) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.areaUrbana = areaUrbana;
        this.areaRural = areaRural;
    }

    public String getDepto() {
        return depto;
    }

    public String getMuni() {
        return muni;
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

    public int getAreaUrbana() {
        return areaUrbana;
    }

    public int getAreaRural() {
        return areaRural;
    }
}