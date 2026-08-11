package hn.gob.ine.listener.model;

public class CalidadVivHogar {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int cantidad;
    private int electricidad;
    private int internet;
    private int tipoSanitarioAlcant;
    private int trenAseo;

    public CalidadVivHogar(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int cantidad, int electricidad,
            int internet, int tipoSanitarioAlcant, int trenAseo) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.cantidad = cantidad;
        this.electricidad = electricidad;
        this.internet = internet;
        this.tipoSanitarioAlcant = tipoSanitarioAlcant;
        this.trenAseo = trenAseo;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getCantidad() { return cantidad; }
    public int getElectricidad() { return electricidad; }
    public int getInternet() { return internet; }
    public int getTipoSanitarioAlcant() { return tipoSanitarioAlcant; }
    public int getTrenAseo() { return trenAseo; }
}