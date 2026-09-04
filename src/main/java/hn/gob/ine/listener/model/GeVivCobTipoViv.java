package hn.gob.ine.listener.model;

public class GeVivCobTipoViv {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private String sector;
    private String segmento;
    private String censista;

    private int vivParticular;
    private int apartamento;
    private int cuarteria;
    private int otroTipo;

    public GeVivCobTipoViv(String depto, String muni, int apoyoMunicipal, int zona, String sector, String segmento, String censista,
                           int vivParticular, int apartamento, int cuarteria, int otroTipo) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.vivParticular = vivParticular;
        this.apartamento = apartamento;
        this.cuarteria = cuarteria;
        this.otroTipo = otroTipo;
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

    public int getVivParticular() {
        return vivParticular;
    }

    public int getApartamento() {
        return apartamento;
    }

    public int getCuarteria() {
        return cuarteria;
    }

    public int getOtroTipo() {
        return otroTipo;
    }
}