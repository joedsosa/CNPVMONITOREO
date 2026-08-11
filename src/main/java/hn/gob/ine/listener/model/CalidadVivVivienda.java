package hn.gob.ine.listener.model;

public class CalidadVivVivienda {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int tipoCasaIndep;
    private int tipoApartamento;
    private int tipoAnexo;
    private int tipoCuartoMeson;
    private int tipoNoConstruido;
    private int tipoRancho;
    private int tipoImprovisado;
    private int tipoOtro;
    private int pisoTierra;
    private int techoTeja;
    private int recibeAgua;
    private int cantidad;

    public CalidadVivVivienda(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int tipoCasaIndep, int tipoApartamento,
            int tipoAnexo, int tipoCuartoMeson, int tipoNoConstruido, int tipoRancho,
            int tipoImprovisado, int tipoOtro, int pisoTierra, int techoTeja,
            int recibeAgua, int cantidad) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.tipoCasaIndep = tipoCasaIndep;
        this.tipoApartamento = tipoApartamento;
        this.tipoAnexo = tipoAnexo;
        this.tipoCuartoMeson = tipoCuartoMeson;
        this.tipoNoConstruido = tipoNoConstruido;
        this.tipoRancho = tipoRancho;
        this.tipoImprovisado = tipoImprovisado;
        this.tipoOtro = tipoOtro;
        this.pisoTierra = pisoTierra;
        this.techoTeja = techoTeja;
        this.recibeAgua = recibeAgua;
        this.cantidad = cantidad;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getTipoCasaIndep() { return tipoCasaIndep; }
    public int getTipoApartamento() { return tipoApartamento; }
    public int getTipoAnexo() { return tipoAnexo; }
    public int getTipoCuartoMeson() { return tipoCuartoMeson; }
    public int getTipoNoConstruido() { return tipoNoConstruido; }
    public int getTipoRancho() { return tipoRancho; }
    public int getTipoImprovisado() { return tipoImprovisado; }
    public int getTipoOtro() { return tipoOtro; }
    public int getPisoTierra() { return pisoTierra; }
    public int getTechoTeja() { return techoTeja; }
    public int getRecibeAgua() { return recibeAgua; }
    public int getCantidad() { return cantidad; }
}