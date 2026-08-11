package hn.gob.ine.listener.model;

public class CalidadEduNivelEducativo {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int cantNoEduc;
    private int cantAlfabetizacion;
    private int cantPrebasica;
    private int cantBasica;
    private int cantMedia;
    private int cantTecSuperior;
    private int cantTecNoSuperior;
    private int cantUniversidad;
    private int cantEspecialidad;
    private int cantMaestria;
    private int cantDoctorado;
    private int cantAnalfabeta;
    private int poblacion15Mas;
    private int totalAniosEstudio;
    private int poblacionParaPromedio;
    private int poblacionNivelEducativo;

    public CalidadEduNivelEducativo(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int cantNoEduc, int cantAlfabetizacion,
            int cantPrebasica, int cantBasica, int cantMedia, int cantTecSuperior,
            int cantTecNoSuperior, int cantUniversidad, int cantEspecialidad,
            int cantMaestria, int cantDoctorado, int cantAnalfabeta,
            int poblacion15Mas, int totalAniosEstudio, int poblacionParaPromedio,
            int poblacionNivelEducativo) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.cantNoEduc = cantNoEduc;
        this.cantAlfabetizacion = cantAlfabetizacion;
        this.cantPrebasica = cantPrebasica;
        this.cantBasica = cantBasica;
        this.cantMedia = cantMedia;
        this.cantTecSuperior = cantTecSuperior;
        this.cantTecNoSuperior = cantTecNoSuperior;
        this.cantUniversidad = cantUniversidad;
        this.cantEspecialidad = cantEspecialidad;
        this.cantMaestria = cantMaestria;
        this.cantDoctorado = cantDoctorado;
        this.cantAnalfabeta = cantAnalfabeta;
        this.poblacion15Mas = poblacion15Mas;
        this.totalAniosEstudio = totalAniosEstudio;
        this.poblacionParaPromedio = poblacionParaPromedio;
        this.poblacionNivelEducativo = poblacionNivelEducativo;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getCantNoEduc() { return cantNoEduc; }
    public int getCantAlfabetizacion() { return cantAlfabetizacion; }
    public int getCantPrebasica() { return cantPrebasica; }
    public int getCantBasica() { return cantBasica; }
    public int getCantMedia() { return cantMedia; }
    public int getCantTecSuperior() { return cantTecSuperior; }
    public int getCantTecNoSuperior() { return cantTecNoSuperior; }
    public int getCantUniversidad() { return cantUniversidad; }
    public int getCantEspecialidad() { return cantEspecialidad; }
    public int getCantMaestria() { return cantMaestria; }
    public int getCantDoctorado() { return cantDoctorado; }
    public int getCantAnalfabeta() { return cantAnalfabeta; }
    public int getPoblacion15Mas() { return poblacion15Mas; }
    public int getTotalAniosEstudio() { return totalAniosEstudio; }
    public int getPoblacionParaPromedio() { return poblacionParaPromedio; }
    public int getPoblacionNivelEducativo() { return poblacionNivelEducativo; }
}