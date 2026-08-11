package hn.gob.ine.listener.model;

public class CalidadMigEmigrantePaisDest {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int cantMujer;
    private int cantHombre;
    private int pais1, pais2, pais3, pais4, paisOtro;
    private int cantidad;
    private int paisEeuuH, paisEeuuM;
    private int paisEspanaH, paisEspanaM;
    private int paisMexicoH, paisMexicoM;
    private int paisCanadaH, paisCanadaM;
    private int paisItaliaH, paisItaliaM;
    private int paisOtroH, paisOtroM;

    public CalidadMigEmigrantePaisDest(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int cantMujer, int cantHombre,
            int pais1, int pais2, int pais3, int pais4, int paisOtro, int cantidad,
            int paisEeuuH, int paisEeuuM, int paisEspanaH, int paisEspanaM,
            int paisMexicoH, int paisMexicoM, int paisCanadaH, int paisCanadaM,
            int paisItaliaH, int paisItaliaM, int paisOtroH, int paisOtroM) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.cantMujer = cantMujer;
        this.cantHombre = cantHombre;
        this.pais1 = pais1;
        this.pais2 = pais2;
        this.pais3 = pais3;
        this.pais4 = pais4;
        this.paisOtro = paisOtro;
        this.cantidad = cantidad;
        this.paisEeuuH = paisEeuuH; this.paisEeuuM = paisEeuuM;
        this.paisEspanaH = paisEspanaH; this.paisEspanaM = paisEspanaM;
        this.paisMexicoH = paisMexicoH; this.paisMexicoM = paisMexicoM;
        this.paisCanadaH = paisCanadaH; this.paisCanadaM = paisCanadaM;
        this.paisItaliaH = paisItaliaH; this.paisItaliaM = paisItaliaM;
        this.paisOtroH = paisOtroH; this.paisOtroM = paisOtroM;
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
    public int getPais1() { return pais1; }
    public int getPais2() { return pais2; }
    public int getPais3() { return pais3; }
    public int getPais4() { return pais4; }
    public int getPaisOtro() { return paisOtro; }
    public int getCantidad() { return cantidad; }
    public int getPaisEeuuH() { return paisEeuuH; }
    public int getPaisEeuuM() { return paisEeuuM; }
    public int getPaisEspanaH() { return paisEspanaH; }
    public int getPaisEspanaM() { return paisEspanaM; }
    public int getPaisMexicoH() { return paisMexicoH; }
    public int getPaisMexicoM() { return paisMexicoM; }
    public int getPaisCanadaH() { return paisCanadaH; }
    public int getPaisCanadaM() { return paisCanadaM; }
    public int getPaisItaliaH() { return paisItaliaH; }
    public int getPaisItaliaM() { return paisItaliaM; }
    public int getPaisOtroH() { return paisOtroH; }
    public int getPaisOtroM() { return paisOtroM; }
}