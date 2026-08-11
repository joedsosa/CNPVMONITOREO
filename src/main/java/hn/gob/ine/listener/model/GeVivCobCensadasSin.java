package hn.gob.ine.listener.model;

public class GeVivCobCensadasSin {

    private String depto;
    private String muni;
    private String sector;
    private String segmento;
    private String censista;

    private int agua;
    private int energia;
    private int sanitario;

    public GeVivCobCensadasSin(String depto, String muni, String sector, String segmento, String censista,
                               int agua, int energia, int sanitario) {
        this.depto = depto;
        this.muni = muni;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.agua = agua;
        this.energia = energia;
        this.sanitario = sanitario;
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

    public int getAgua() {
        return agua;
    }

    public int getEnergia() {
        return energia;
    }

    public int getSanitario() {
        return sanitario;
    }
}