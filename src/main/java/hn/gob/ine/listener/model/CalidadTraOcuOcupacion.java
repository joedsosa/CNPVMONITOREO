package hn.gob.ine.listener.model;

public class CalidadTraOcuOcupacion {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int cantHombre;
    private int cantMujer;
    private int cantPOcupadaM;
    private int cantPOcupadaH;
    private int cantPDesocupadaM;
    private int cantPDesocupadaH;
    private int cantRetiradaEdad15M;
    private int cantRetiradaEdad15H;
    private int cantCuidadoHogarM;
    private int cantCuidadoHogarH;
    private int cantSectorPublicoM;
    private int cantSectorPublicoH;
    private int cantSectorPrivadoM;
    private int cantSectorPrivadoH;
    private int cantEmpleoDomesticoM;
    private int cantEmpleoDomesticoH;
    private int poblacion15MasM;
    private int poblacion15MasH;

    public CalidadTraOcuOcupacion(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int cantHombre, int cantMujer,
            int cantPOcupadaM, int cantPOcupadaH, int cantPDesocupadaM, int cantPDesocupadaH,
            int cantRetiradaEdad15M, int cantRetiradaEdad15H, int cantCuidadoHogarM,
            int cantCuidadoHogarH, int cantSectorPublicoM, int cantSectorPublicoH,
            int cantSectorPrivadoM, int cantSectorPrivadoH, int cantEmpleoDomesticoM,
            int cantEmpleoDomesticoH, int poblacion15MasM, int poblacion15MasH) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.cantHombre = cantHombre;
        this.cantMujer = cantMujer;
        this.cantPOcupadaM = cantPOcupadaM;
        this.cantPOcupadaH = cantPOcupadaH;
        this.cantPDesocupadaM = cantPDesocupadaM;
        this.cantPDesocupadaH = cantPDesocupadaH;
        this.cantRetiradaEdad15M = cantRetiradaEdad15M;
        this.cantRetiradaEdad15H = cantRetiradaEdad15H;
        this.cantCuidadoHogarM = cantCuidadoHogarM;
        this.cantCuidadoHogarH = cantCuidadoHogarH;
        this.cantSectorPublicoM = cantSectorPublicoM;
        this.cantSectorPublicoH = cantSectorPublicoH;
        this.cantSectorPrivadoM = cantSectorPrivadoM;
        this.cantSectorPrivadoH = cantSectorPrivadoH;
        this.cantEmpleoDomesticoM = cantEmpleoDomesticoM;
        this.cantEmpleoDomesticoH = cantEmpleoDomesticoH;
        this.poblacion15MasM = poblacion15MasM;
        this.poblacion15MasH = poblacion15MasH;
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
    public int getCantPOcupadaM() { return cantPOcupadaM; }
    public int getCantPOcupadaH() { return cantPOcupadaH; }
    public int getCantPDesocupadaM() { return cantPDesocupadaM; }
    public int getCantPDesocupadaH() { return cantPDesocupadaH; }
    public int getCantRetiradaEdad15M() { return cantRetiradaEdad15M; }
    public int getCantRetiradaEdad15H() { return cantRetiradaEdad15H; }
    public int getCantCuidadoHogarM() { return cantCuidadoHogarM; }
    public int getCantCuidadoHogarH() { return cantCuidadoHogarH; }
    public int getCantSectorPublicoM() { return cantSectorPublicoM; }
    public int getCantSectorPublicoH() { return cantSectorPublicoH; }
    public int getCantSectorPrivadoM() { return cantSectorPrivadoM; }
    public int getCantSectorPrivadoH() { return cantSectorPrivadoH; }
    public int getCantEmpleoDomesticoM() { return cantEmpleoDomesticoM; }
    public int getCantEmpleoDomesticoH() { return cantEmpleoDomesticoH; }
    public int getPoblacion15MasM() { return poblacion15MasM; }
    public int getPoblacion15MasH() { return poblacion15MasH; }
}