package hn.gob.ine.listener.model;

public class CalidadCompHogEnvejecimiento {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int noInscritaRnpH;
    private int noInscritaRnpM;
    private int cantMayor60H;
    private int cantMayor60M;
    private int cantHombre;
    private int cantMujer;
    private int cant100MasH;
    private int cant100MasM;
    private int cantidad;
    private int cantMenor15H;
    private int cantMenor15M;

    public CalidadCompHogEnvejecimiento(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int noInscritaRnpH, int noInscritaRnpM,
            int cantMayor60H, int cantMayor60M, int cantHombre, int cantMujer,
            int cant100MasH, int cant100MasM, int cantidad, int cantMenor15H, int cantMenor15M) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.noInscritaRnpH = noInscritaRnpH;
        this.noInscritaRnpM = noInscritaRnpM;
        this.cantMayor60H = cantMayor60H;
        this.cantMayor60M = cantMayor60M;
        this.cantHombre = cantHombre;
        this.cantMujer = cantMujer;
        this.cant100MasH = cant100MasH;
        this.cant100MasM = cant100MasM;
        this.cantidad = cantidad;
        this.cantMenor15H = cantMenor15H;
        this.cantMenor15M = cantMenor15M;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getNoInscritaRnpH() { return noInscritaRnpH; }
    public int getNoInscritaRnpM() { return noInscritaRnpM; }
    public int getCantMayor60H() { return cantMayor60H; }
    public int getCantMayor60M() { return cantMayor60M; }
    public int getCantHombre() { return cantHombre; }
    public int getCantMujer() { return cantMujer; }
    public int getCant100MasH() { return cant100MasH; }
    public int getCant100MasM() { return cant100MasM; }
    public int getCantidad() { return cantidad; }
    public int getCantMenor15H() { return cantMenor15H; }
    public int getCantMenor15M() { return cantMenor15M; }
}