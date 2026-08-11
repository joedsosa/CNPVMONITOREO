package hn.gob.ine.listener.model;

import java.sql.Date;

public class CobEvolucionCobertura {

    private String depto;
    private String muni;
    private String sector;
    private String segmento;
    private String censista;

    private int avanceDia;
    private int vivOcupadasDia;
    private Date fecha;

    public CobEvolucionCobertura(String depto, String muni, String sector, String segmento, String censista,
                                  int avanceDia, int vivOcupadasDia, Date fecha) {
        this.depto = depto;
        this.muni = muni;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.avanceDia = avanceDia;
        this.vivOcupadasDia = vivOcupadasDia;
        this.fecha = fecha;
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

    public int getAvanceDia() {
        return avanceDia;
    }

    public int getVivOcupadasDia() {
        return vivOcupadasDia;
    }

    public Date getFecha() {
        return fecha;
    }
}