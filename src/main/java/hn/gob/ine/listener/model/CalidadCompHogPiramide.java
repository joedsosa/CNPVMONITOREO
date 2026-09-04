package hn.gob.ine.listener.model;

public class CalidadCompHogPiramide {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int edad70_74H, edad70_74M;
    private int edad75_79H, edad75_79M;
    private int edad80_84H, edad80_84M;
    private int edad85_89H, edad85_89M;
    private int edad90_94H, edad90_94M;
    private int edad95_99H, edad95_99M;
    private int edad100MasH, edad100MasM;

    public CalidadCompHogPiramide(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int edad70_74H, int edad70_74M, int edad75_79H, int edad75_79M,
            int edad80_84H, int edad80_84M, int edad85_89H, int edad85_89M,
            int edad90_94H, int edad90_94M, int edad95_99H, int edad95_99M,
            int edad100MasH, int edad100MasM) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.edad70_74H = edad70_74H; this.edad70_74M = edad70_74M;
        this.edad75_79H = edad75_79H; this.edad75_79M = edad75_79M;
        this.edad80_84H = edad80_84H; this.edad80_84M = edad80_84M;
        this.edad85_89H = edad85_89H; this.edad85_89M = edad85_89M;
        this.edad90_94H = edad90_94H; this.edad90_94M = edad90_94M;
        this.edad95_99H = edad95_99H; this.edad95_99M = edad95_99M;
        this.edad100MasH = edad100MasH; this.edad100MasM = edad100MasM;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getEdad70_74H() { return edad70_74H; }
    public int getEdad70_74M() { return edad70_74M; }
    public int getEdad75_79H() { return edad75_79H; }
    public int getEdad75_79M() { return edad75_79M; }
    public int getEdad80_84H() { return edad80_84H; }
    public int getEdad80_84M() { return edad80_84M; }
    public int getEdad85_89H() { return edad85_89H; }
    public int getEdad85_89M() { return edad85_89M; }
    public int getEdad90_94H() { return edad90_94H; }
    public int getEdad90_94M() { return edad90_94M; }
    public int getEdad95_99H() { return edad95_99H; }
    public int getEdad95_99M() { return edad95_99M; }
    public int getEdad100MasH() { return edad100MasH; }
    public int getEdad100MasM() { return edad100MasM; }
}