package hn.gob.ine.listener.model;

public class CobTotalViviendasOcupAusentes {

    private String depto;
    private String muni;
    private String sector;
    private String segmento;

    private int cantidadOcupadasAusente;
    private int cantidad;

    public CobTotalViviendasOcupAusentes(String depto, String muni, String sector, String segmento,
                                         int cantidadOcupadasAusente, int cantidad) {
        this.depto = depto;
        this.muni = muni;
        this.sector = sector;
        this.segmento = segmento;
        this.cantidadOcupadasAusente = cantidadOcupadasAusente;
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

    public int getCantidadOcupadasAusente() {
        return cantidadOcupadasAusente;
    }

    public int getCantidad() {
        return cantidad;
    }
}