package hn.gob.ine.listener.model;

public class CalidadNacResOtroLugar {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int cantNacOtroMuni;
    private int cantNacOtroPais;
    private int cantNac5anios;
    private int cantNacOtroMuniH;
    private int cantNacOtroMuniM;
    private int cantNacOtroPaisH;
    private int cantNacOtroPaisM;
    private int cantNac5aniosH;
    private int cantNac5aniosM;

    public CalidadNacResOtroLugar(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int cantNacOtroMuni, int cantNacOtroPais,
            int cantNac5anios, int cantNacOtroMuniH, int cantNacOtroMuniM,
            int cantNacOtroPaisH, int cantNacOtroPaisM, int cantNac5aniosH, int cantNac5aniosM) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.cantNacOtroMuni = cantNacOtroMuni;
        this.cantNacOtroPais = cantNacOtroPais;
        this.cantNac5anios = cantNac5anios;
        this.cantNacOtroMuniH = cantNacOtroMuniH;
        this.cantNacOtroMuniM = cantNacOtroMuniM;
        this.cantNacOtroPaisH = cantNacOtroPaisH;
        this.cantNacOtroPaisM = cantNacOtroPaisM;
        this.cantNac5aniosH = cantNac5aniosH;
        this.cantNac5aniosM = cantNac5aniosM;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getCantNacOtroMuni() { return cantNacOtroMuni; }
    public int getCantNacOtroPais() { return cantNacOtroPais; }
    public int getCantNac5anios() { return cantNac5anios; }
    public int getCantNacOtroMuniH() { return cantNacOtroMuniH; }
    public int getCantNacOtroMuniM() { return cantNacOtroMuniM; }
    public int getCantNacOtroPaisH() { return cantNacOtroPaisH; }
    public int getCantNacOtroPaisM() { return cantNacOtroPaisM; }
    public int getCantNac5aniosH() { return cantNac5aniosH; }
    public int getCantNac5aniosM() { return cantNac5aniosM; }
}