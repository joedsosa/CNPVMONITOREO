package hn.gob.ine.listener.model;

public class CobCensistaProductividad {

    private String depto;
    private String muni;
    private int apoyoMunicipal;
    private int zona;
    private int sector;
    private String segmento;
    private String censista;
    private int estructurasTrabajadas;
    private int estructurasNuevas;
    private int totalViviendasParticulares;
    private int viviendasDesocupadas;
    private int viviendasOcupAusentes;
    private int rechazos;
    private int transformadas;
    private int referencias;
    private int cuestionariosEfectivos;
    private int cantidadVisitas;
    private int promedioDuracionSeg;
    private int maxDuracionSeg;
    private int diasTrabajados;
    private int cantidadHogaresUnipersonales;
    private int cantidadHogaresMas3;
    private int cantidadHogares;
    private int cantidadPersonas;
    private int area;

    public CobCensistaProductividad(String depto, String muni, int apoyoMunicipal, int zona, int sector,
            String segmento, String censista, int estructurasTrabajadas, int estructurasNuevas,
            int totalViviendasParticulares,
            int viviendasDesocupadas, int viviendasOcupAusentes, int rechazos, int transformadas, int referencias, int cuestionariosEfectivos,
            int cantidadVisitas, int promedioDuracionSeg, int maxDuracionSeg, int diasTrabajados,
            int cantidadHogaresUnipersonales, int cantidadHogaresMas3, int cantidadHogares, int cantidadPersonas, int area) {
        this.depto = depto;
        this.muni = muni;
        this.apoyoMunicipal = apoyoMunicipal;
        this.zona = zona;
        this.sector = sector;
        this.segmento = segmento;
        this.censista = censista;
        this.estructurasTrabajadas = estructurasTrabajadas;
        this.estructurasNuevas = estructurasNuevas;
        this.totalViviendasParticulares = totalViviendasParticulares;
        this.viviendasDesocupadas = viviendasDesocupadas;
        this.viviendasOcupAusentes = viviendasOcupAusentes;
        this.rechazos = rechazos;
        this.transformadas = transformadas;
        this.referencias = referencias;
        this.cuestionariosEfectivos = cuestionariosEfectivos;
        this.cantidadVisitas = cantidadVisitas;
        this.promedioDuracionSeg = promedioDuracionSeg;
        this.maxDuracionSeg = maxDuracionSeg;
        this.diasTrabajados = diasTrabajados;
        this.cantidadHogaresUnipersonales = cantidadHogaresUnipersonales;
        this.cantidadHogaresMas3 = cantidadHogaresMas3;
        this.cantidadHogares = cantidadHogares;
        this.cantidadPersonas = cantidadPersonas;
        this.area = area;
    }

    public String getDepto() { return depto; }
    public String getMuni() { return muni; }
    public int getApoyoMunicipal() { return apoyoMunicipal; }
    public int getZona() { return zona; }
    public int getSector() { return sector; }
    public String getSegmento() { return segmento; }
    public String getCensista() { return censista; }
    public int getEstructurasTrabajadas() { return estructurasTrabajadas; }
    public int getEstructurasNuevas() { return estructurasNuevas; }
    public int getTotalViviendasParticulares() { return totalViviendasParticulares; }
    public int getViviendasDesocupadas() { return viviendasDesocupadas; }
    public int getViviendasOcupAusentes() { return viviendasOcupAusentes; }
    public int getRechazos() { return rechazos; }
    public int getTransformadas() { return transformadas; }
    public int getReferencias() { return referencias; }
    public int getCuestionariosEfectivos() { return cuestionariosEfectivos; }
    public int getCantidadVisitas() { return cantidadVisitas; }
    public int getPromedioDuracionSeg() { return promedioDuracionSeg; }
    public int getMaxDuracionSeg() { return maxDuracionSeg; }
    public int getDiasTrabajados() { return diasTrabajados; }
    public int getCantidadHogaresUnipersonales() { return cantidadHogaresUnipersonales; }
    public int getCantidadHogaresMas3() { return cantidadHogaresMas3; }
    public int getCantidadHogares() { return cantidadHogares; }
    public int getCantidadPersonas() { return cantidadPersonas; }
    public int getArea() { return area; }
}
