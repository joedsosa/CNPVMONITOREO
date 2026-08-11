package hn.gob.ine.listener.model;

public class TotalHogViv {
    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int hogar;
    private int vivienda;
    private int cantidadPersonas;

    public TotalHogViv(String depto, String muni, int apoyoMunicipal, int zona, int sector, String segmento, String censista,
                       int hogar, int vivienda, int cantidadPersonas) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.hogar = hogar;
        this.vivienda = vivienda;
        this.cantidadPersonas = cantidadPersonas;
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

    public int getSector() {
        return sector;
    }

    public String getSegmento() {
        return segmento;
    }

    public String getCensista() {
        return censista;
    }

    public int getHogar() {
        return hogar;
    }

    public int getVivienda() {
        return vivienda;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }
}