package hn.gob.ine.listener.model;

public class CalidadEtnIndigenaAfro {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int cantMujer;
    private int cantHombre;
    private int cantIndigena;
    private int cantAfro;
    private int cantIndigenaH;
    private int cantIndigenaM;
    private int cantAfroH;
    private int cantAfroM;

    public CalidadEtnIndigenaAfro(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int cantMujer, int cantHombre,
            int cantIndigena, int cantAfro, int cantIndigenaH, int cantIndigenaM,
            int cantAfroH, int cantAfroM) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.cantMujer = cantMujer;
        this.cantHombre = cantHombre;
        this.cantIndigena = cantIndigena;
        this.cantAfro = cantAfro;
        this.cantIndigenaH = cantIndigenaH;
        this.cantIndigenaM = cantIndigenaM;
        this.cantAfroH = cantAfroH;
        this.cantAfroM = cantAfroM;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getCantMujer() { return cantMujer; }
    public int getCantHombre() { return cantHombre; }
    public int getCantIndigena() { return cantIndigena; }
    public int getCantAfro() { return cantAfro; }
    public int getCantIndigenaH() { return cantIndigenaH; }
    public int getCantIndigenaM() { return cantIndigenaM; }
    public int getCantAfroH() { return cantAfroH; }
    public int getCantAfroM() { return cantAfroM; }
}