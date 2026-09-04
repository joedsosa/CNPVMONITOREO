package hn.gob.ine.listener.model;

public class CobTotalViviendasOcupAusentes {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private String sector;
    private String segmento;
    private String censista;

    private int cantidadOcupadasAusente;
    private int cantidad;

    public CobTotalViviendasOcupAusentes(String depto, String muni, int apoyoMunicipal, int zona, String sector, String segmento, String censista,
                                         int cantidadOcupadasAusente, int cantidad) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.cantidadOcupadasAusente = cantidadOcupadasAusente;
        this.cantidad = cantidad;
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

    public int getCantidadOcupadasAusente() {
        return cantidadOcupadasAusente;
    }

    public int getCantidad() {
        return cantidad;
    }
}