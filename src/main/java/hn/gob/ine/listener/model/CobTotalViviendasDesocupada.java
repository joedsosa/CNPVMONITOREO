package hn.gob.ine.listener.model;

public class CobTotalViviendasDesocupada {

    private String depto;
    private String muni;
    private String sector;
    private String segmento;

    private int cantidadDesocupada;
    private int cantidad;

    public CobTotalViviendasDesocupada(String depto, String muni, String sector, String segmento,
                                       int cantidadDesocupada, int cantidad) {
        this.depto = depto;
        this.muni = muni;
        this.sector = sector;
        this.segmento = segmento;
        this.cantidadDesocupada = cantidadDesocupada;
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

    public int getCantidadDesocupada() {
        return cantidadDesocupada;
    }

    public int getCantidad() {
        return cantidad;
    }
}