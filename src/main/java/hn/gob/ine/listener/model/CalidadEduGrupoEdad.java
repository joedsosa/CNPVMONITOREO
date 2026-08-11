package hn.gob.ine.listener.model;

public class CalidadEduGrupoEdad {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int eduEdad5_6;
    private int eduEdad5_12;
    private int eduEdad5_17;
    private int eduEdad6_12;
    private int eduEdad7_12;
    private int eduEdad13_17;
    private int poblacion6_12;
    private int poblacion7_12;
    private int poblacion5_6;
    private int poblacion13_17;
    private int poblacion5_17;
    private int eduEdad3Mas;
    private int poblacion3Mas;

    public CalidadEduGrupoEdad(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int eduEdad5_6, int eduEdad5_12,
            int eduEdad5_17, int eduEdad6_12, int eduEdad7_12, int eduEdad13_17,
            int poblacion6_12, int poblacion7_12, int poblacion5_6,
            int poblacion13_17, int poblacion5_17, int eduEdad3Mas, int poblacion3Mas) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.eduEdad5_6 = eduEdad5_6;
        this.eduEdad5_12 = eduEdad5_12;
        this.eduEdad5_17 = eduEdad5_17;
        this.eduEdad6_12 = eduEdad6_12;
        this.eduEdad7_12 = eduEdad7_12;
        this.eduEdad13_17 = eduEdad13_17;
        this.poblacion6_12 = poblacion6_12;
        this.poblacion7_12 = poblacion7_12;
        this.poblacion5_6 = poblacion5_6;
        this.poblacion13_17 = poblacion13_17;
        this.poblacion5_17 = poblacion5_17;
        this.eduEdad3Mas = eduEdad3Mas;
        this.poblacion3Mas = poblacion3Mas;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getEduEdad5_6() { return eduEdad5_6; }
    public int getEduEdad5_12() { return eduEdad5_12; }
    public int getEduEdad5_17() { return eduEdad5_17; }
    public int getEduEdad6_12() { return eduEdad6_12; }
    public int getEduEdad7_12() { return eduEdad7_12; }
    public int getEduEdad13_17() { return eduEdad13_17; }
    public int getPoblacion6_12() { return poblacion6_12; }
    public int getPoblacion7_12() { return poblacion7_12; }
    public int getPoblacion5_6() { return poblacion5_6; }
    public int getPoblacion13_17() { return poblacion13_17; }
    public int getPoblacion5_17() { return poblacion5_17; }
    public int getEduEdad3Mas() { return eduEdad3Mas; }
    public int getPoblacion3Mas() { return poblacion3Mas; }
}