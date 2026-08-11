package hn.gob.ine.listener.model;

public class CobCensistaPorVivienda {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int area;
    private int totalViviendasParticulares;
    private int totalViviendasRealizadas;
    private int personasAusentes;
    private int viviendasConRechazo;
    private int viviendasTransformadas;
    private int boletasEfectivas;
    private int entrevistasRechazadas;

    public CobCensistaPorVivienda(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int area, int totalViviendasParticulares,
            int totalViviendasRealizadas, int personasAusentes, int viviendasConRechazo,
            int viviendasTransformadas, int boletasEfectivas, int entrevistasRechazadas) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.area = area;
        this.totalViviendasParticulares = totalViviendasParticulares;
        this.totalViviendasRealizadas = totalViviendasRealizadas;
        this.personasAusentes = personasAusentes;
        this.viviendasConRechazo = viviendasConRechazo;
        this.viviendasTransformadas = viviendasTransformadas;
        this.boletasEfectivas = boletasEfectivas;
        this.entrevistasRechazadas = entrevistasRechazadas;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getArea() { return area; }
    public int getTotalViviendasParticulares() { return totalViviendasParticulares; }
    public int getTotalViviendasRealizadas() { return totalViviendasRealizadas; }
    public int getPersonasAusentes() { return personasAusentes; }
    public int getViviendasConRechazo() { return viviendasConRechazo; }
    public int getViviendasTransformadas() { return viviendasTransformadas; }
    public int getBoletasEfectivas() { return boletasEfectivas; }
    public int getEntrevistasRechazadas() { return entrevistasRechazadas; }
}
