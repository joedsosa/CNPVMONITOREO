package hn.gob.ine.listener.model;

public class CobCondicionVivienda {

    private String depto;
    private String muni;
    private String sector;
    private String segmento;
    private String censista;

    private int ocupadaPresentes;
    private int ocupadasPresentes;
    private int rechazadas;
    private int pendientes;

    public CobCondicionVivienda(String depto, String muni, String sector, String segmento, String censista,
                                int ocupadaPresentes, int ocupadasPresentes, int rechazadas, int pendientes) {
        this.depto = depto;
        this.muni = muni;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.ocupadaPresentes = ocupadaPresentes;
        this.ocupadasPresentes = ocupadasPresentes;
        this.rechazadas = rechazadas;
        this.pendientes = pendientes;
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

    public String getCensista() {
        return censista;
    }

    public int getOcupadaPresentes() {
        return ocupadaPresentes;
    }

    public int getOcupadasPresentes() {
        return ocupadasPresentes;
    }

    public int getRechazadas() {
        return rechazadas;
    }

    public int getPendientes() {
        return pendientes;
    }
}