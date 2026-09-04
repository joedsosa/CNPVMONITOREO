package hn.gob.ine.listener.model;

public class CobTotalViviendasOcupadas {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private String sector;
    private String segmento;
    private String censista;

    private int cantidadOcupadas;
    private int cantidadCensadasBoleta;
    private int cantidadOcupDesoRechazo;
    private int cantidad;

    public CobTotalViviendasOcupadas(String depto, String muni, int apoyoMunicipal, int zona, String sector, String segmento, String censista,
                                     int cantidadOcupadas, int cantidadCensadasBoleta, int cantidadOcupDesoRechazo, int cantidad) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.cantidadOcupadas = cantidadOcupadas;
        this.cantidadCensadasBoleta = cantidadCensadasBoleta;
        this.cantidadOcupDesoRechazo = cantidadOcupDesoRechazo;
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

    public int getCantidadOcupadas() {
        return cantidadOcupadas;
    }

    public int getCantidadCensadasBoleta() {
        return cantidadCensadasBoleta;
    }

    public int getCantidadOcupDesoRechazo() {
        return cantidadOcupDesoRechazo;
    }

    public int getCantidad() {
        return cantidad;
    }
}