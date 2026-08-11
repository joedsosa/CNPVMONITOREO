package hn.gob.ine.listener.model;

public class CalidadMorMortalidad {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int cantHombre;
    private int cantMujer;
    private int edad0H, edad0M;
    private int edad1_4H, edad1_4M;
    private int edad5_14H, edad5_14M;
    private int edad15_24H, edad15_24M;
    private int edad25_34H, edad25_34M;
    private int edad35_44H, edad35_44M;
    private int edad45_54H, edad45_54M;
    private int edad55_64H, edad55_64M;
    private int edad65_74H, edad65_74M;
    private int edad75MasH, edad75MasM;
    private int cantidad;
    private int hogarConUnFallecido;

    public CalidadMorMortalidad(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int cantHombre, int cantMujer,
            int edad0H, int edad0M, int edad1_4H, int edad1_4M,
            int edad5_14H, int edad5_14M, int edad15_24H, int edad15_24M,
            int edad25_34H, int edad25_34M, int edad35_44H, int edad35_44M,
            int edad45_54H, int edad45_54M, int edad55_64H, int edad55_64M,
            int edad65_74H, int edad65_74M, int edad75MasH, int edad75MasM,
            int cantidad, int hogarConUnFallecido) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.cantHombre = cantHombre;
        this.cantMujer = cantMujer;
        this.edad0H = edad0H; this.edad0M = edad0M;
        this.edad1_4H = edad1_4H; this.edad1_4M = edad1_4M;
        this.edad5_14H = edad5_14H; this.edad5_14M = edad5_14M;
        this.edad15_24H = edad15_24H; this.edad15_24M = edad15_24M;
        this.edad25_34H = edad25_34H; this.edad25_34M = edad25_34M;
        this.edad35_44H = edad35_44H; this.edad35_44M = edad35_44M;
        this.edad45_54H = edad45_54H; this.edad45_54M = edad45_54M;
        this.edad55_64H = edad55_64H; this.edad55_64M = edad55_64M;
        this.edad65_74H = edad65_74H; this.edad65_74M = edad65_74M;
        this.edad75MasH = edad75MasH; this.edad75MasM = edad75MasM;
        this.cantidad = cantidad;
        this.hogarConUnFallecido = hogarConUnFallecido;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getCantHombre() { return cantHombre; }
    public int getCantMujer() { return cantMujer; }
    public int getEdad0H() { return edad0H; }
    public int getEdad0M() { return edad0M; }
    public int getEdad1_4H() { return edad1_4H; }
    public int getEdad1_4M() { return edad1_4M; }
    public int getEdad5_14H() { return edad5_14H; }
    public int getEdad5_14M() { return edad5_14M; }
    public int getEdad15_24H() { return edad15_24H; }
    public int getEdad15_24M() { return edad15_24M; }
    public int getEdad25_34H() { return edad25_34H; }
    public int getEdad25_34M() { return edad25_34M; }
    public int getEdad35_44H() { return edad35_44H; }
    public int getEdad35_44M() { return edad35_44M; }
    public int getEdad45_54H() { return edad45_54H; }
    public int getEdad45_54M() { return edad45_54M; }
    public int getEdad55_64H() { return edad55_64H; }
    public int getEdad55_64M() { return edad55_64M; }
    public int getEdad65_74H() { return edad65_74H; }
    public int getEdad65_74M() { return edad65_74M; }
    public int getEdad75MasH() { return edad75MasH; }
    public int getEdad75MasM() { return edad75MasM; }
    public int getCantidad() { return cantidad; }
    public int getHogarConUnFallecido() { return hogarConUnFallecido; }
}