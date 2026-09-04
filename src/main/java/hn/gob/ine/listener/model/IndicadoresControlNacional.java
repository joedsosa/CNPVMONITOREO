package hn.gob.ine.listener.model;

public class IndicadoresControlNacional {

    private int avanceEstructurasNum;
    private int avanceEstructurasDen;
    private double avanceEstructurasPct;

    private int ocupadasPresentesNum;
    private int ocupadasPresentesDen;
    private double ocupadasPresentesPct;

    private int ocupadasAusentesNum;
    private int ocupadasAusentesDen;
    private double ocupadasAusentesPct;

    private int desocupadasNum;
    private int desocupadasDen;
    private double desocupadasPct;

    private int distOcupPresentes;
    private int distOcupAusentes;
    private int distRechazadas;
    private int distTotal;

    public IndicadoresControlNacional(
            int avanceEstructurasNum, int avanceEstructurasDen, double avanceEstructurasPct,
            int ocupadasPresentesNum, int ocupadasPresentesDen, double ocupadasPresentesPct,
            int ocupadasAusentesNum, int ocupadasAusentesDen, double ocupadasAusentesPct,
            int desocupadasNum, int desocupadasDen, double desocupadasPct,
            int distOcupPresentes, int distOcupAusentes, int distRechazadas, int distTotal) {
        this.avanceEstructurasNum = avanceEstructurasNum;
        this.avanceEstructurasDen = avanceEstructurasDen;
        this.avanceEstructurasPct = avanceEstructurasPct;
        this.ocupadasPresentesNum = ocupadasPresentesNum;
        this.ocupadasPresentesDen = ocupadasPresentesDen;
        this.ocupadasPresentesPct = ocupadasPresentesPct;
        this.ocupadasAusentesNum = ocupadasAusentesNum;
        this.ocupadasAusentesDen = ocupadasAusentesDen;
        this.ocupadasAusentesPct = ocupadasAusentesPct;
        this.desocupadasNum = desocupadasNum;
        this.desocupadasDen = desocupadasDen;
        this.desocupadasPct = desocupadasPct;
        this.distOcupPresentes = distOcupPresentes;
        this.distOcupAusentes = distOcupAusentes;
        this.distRechazadas = distRechazadas;
        this.distTotal = distTotal;
    }

    public int getAvanceEstructurasNum() { return avanceEstructurasNum; }
    public int getAvanceEstructurasDen() { return avanceEstructurasDen; }
    public double getAvanceEstructurasPct() { return avanceEstructurasPct; }

    public int getOcupadasPresentesNum() { return ocupadasPresentesNum; }
    public int getOcupadasPresentesDen() { return ocupadasPresentesDen; }
    public double getOcupadasPresentesPct() { return ocupadasPresentesPct; }

    public int getOcupadasAusentesNum() { return ocupadasAusentesNum; }
    public int getOcupadasAusentesDen() { return ocupadasAusentesDen; }
    public double getOcupadasAusentesPct() { return ocupadasAusentesPct; }

    public int getDesocupadasNum() { return desocupadasNum; }
    public int getDesocupadasDen() { return desocupadasDen; }
    public double getDesocupadasPct() { return desocupadasPct; }

    public int getDistOcupPresentes() { return distOcupPresentes; }
    public int getDistOcupAusentes() { return distOcupAusentes; }
    public int getDistRechazadas() { return distRechazadas; }
    public int getDistTotal() { return distTotal; }
}
