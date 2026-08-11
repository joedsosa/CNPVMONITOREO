package hn.gob.ine.listener.model;

public class CobTotalViviendasOcupadas {

    private String depto;
    private String muni;
    private String sector;
    private String segmento;

    private int cantidadOcupadas;
    private int cantidad;

    public CobTotalViviendasOcupadas(String depto, String muni, String sector, String segmento,
                                     int cantidadOcupadas, int cantidad) {
        this.depto = depto;
        this.muni = muni;
        this.sector = sector;
        this.segmento = segmento;
        this.cantidadOcupadas = cantidadOcupadas;
        this.cantidad = cantidad;
    }

    public String getDepto() {
        return depto;
    }

    public String getMuni() {
        return muni;
    }

    public String getSector() {
        return sector;
    }

    public String getSegmento() {
        return segmento;
    }

    public int getCantidadOcupadas() {
        return cantidadOcupadas;
    }

    public int getCantidad() {
        return cantidad;
    }
}