package hn.gob.ine.listener.model;

public class LlaveCensista {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;

    public LlaveCensista(String depto, String muni, int apoyoMunicipal, int zona, int sector, String segmento, String censista) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
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

    @Override
    public String toString() {
        return "LlaveCensista{" +
                "depto='" + depto + '\'' +
                ", muni='" + muni + '\'' +
                ", apoyoMunicipal=" + apoyoMunicipal +
                ", zona=" + zona +
                ", sector=" + sector +
                ", segmento='" + segmento + '\'' +
                ", censista='" + censista + '\'' +
                '}';
    }
}