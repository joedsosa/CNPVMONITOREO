package hn.gob.ine.listener.model;

public class CobTotalViviendasParticulares {

    private String depto;
    private String muni;
    private String sector;
    private String segmento;

    private int cantidadParticulares;
    private int cantidad;

    public CobTotalViviendasParticulares(String depto, String muni, String sector, String segmento,
                                         int cantidadParticulares, int cantidad) {
        this.depto = depto;
        this.muni = muni;
        this.sector = sector;
        this.segmento = segmento;
        this.cantidadParticulares = cantidadParticulares;
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

    public int getCantidadParticulares() {
        return cantidadParticulares;
    }

    public int getCantidad() {
        return cantidad;
    }
}