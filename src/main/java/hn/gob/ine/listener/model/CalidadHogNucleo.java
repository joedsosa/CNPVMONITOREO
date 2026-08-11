package hn.gob.ine.listener.model;

public class CalidadHogNucleo {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int jefeMujer;
    private int jefeHombre;
    private int biparentalSinHijos;
    private int biparentalConHijos;
    private int unipersonal;
    private int monoparentalJefeMujer;

    public CalidadHogNucleo(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int jefeMujer, int jefeHombre,
            int biparentalSinHijos, int biparentalConHijos, int unipersonal,
            int monoparentalJefeMujer) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.jefeMujer = jefeMujer;
        this.jefeHombre = jefeHombre;
        this.biparentalSinHijos = biparentalSinHijos;
        this.biparentalConHijos = biparentalConHijos;
        this.unipersonal = unipersonal;
        this.monoparentalJefeMujer = monoparentalJefeMujer;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getJefeMujer() { return jefeMujer; }
    public int getJefeHombre() { return jefeHombre; }
    public int getBiparentalSinHijos() { return biparentalSinHijos; }
    public int getBiparentalConHijos() { return biparentalConHijos; }
    public int getUnipersonal() { return unipersonal; }
    public int getMonoparentalJefeMujer() { return monoparentalJefeMujer; }
}