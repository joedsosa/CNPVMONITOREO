package hn.gob.ine.listener.dao;

import hn.gob.ine.listener.config.DataSourceFactory;
import hn.gob.ine.listener.model.CalidadCompHogEnvejecimiento;
import hn.gob.ine.listener.model.CalidadCompHogPiramide;
import hn.gob.ine.listener.model.CalidadConyEstado;
import hn.gob.ine.listener.model.CalidadDiscLimitacion;
import hn.gob.ine.listener.model.CobArea;
import hn.gob.ine.listener.model.CobCoberturaDepartamentos;
import hn.gob.ine.listener.model.CobCondicionVivienda;
import hn.gob.ine.listener.model.CobEvolucionCobertura;
import hn.gob.ine.listener.model.TotalHogViv;
import hn.gob.ine.listener.model.GeVivCobViviendasCensadas;
import hn.gob.ine.listener.model.GeVivCobHogaresCensados;
import hn.gob.ine.listener.model.GeVivCobTipoViv;
import hn.gob.ine.listener.model.CobTotalViviendasOcupadas;
import hn.gob.ine.listener.model.CobTotalViviendasDesocupada;
import hn.gob.ine.listener.model.CobTotalViviendasOcupAusentes;
import hn.gob.ine.listener.model.CobTotalViviendasParticulares;
import hn.gob.ine.listener.model.GeVivCobCensadasSin;
import hn.gob.ine.listener.model.GeVivCobDeptosCompletados;
import hn.gob.ine.listener.model.GeVivCobMunisCompletados;
import hn.gob.ine.listener.model.CalidadVivVivienda;
import hn.gob.ine.listener.model.CalidadVivHogar;
import hn.gob.ine.listener.model.CalidadHogServicios;
import hn.gob.ine.listener.model.CalidadHogNucleo;
import hn.gob.ine.listener.model.CalidadHogHacinamiento;
import hn.gob.ine.listener.model.CalidadEduGrupoEdad;
import hn.gob.ine.listener.model.CalidadEduNivelEducativo;
import hn.gob.ine.listener.model.CalidadEtnIndigenaAfro;
import hn.gob.ine.listener.model.CalidadFecFecundidad;
import hn.gob.ine.listener.model.CalidadMigEmigranteGenero;
import hn.gob.ine.listener.model.CalidadMigEmigrantePaisDest;
import hn.gob.ine.listener.model.CalidadMorMortalidad;
import hn.gob.ine.listener.model.CalidadNacResOtroLugar;
import hn.gob.ine.listener.model.CalidadTicAcceso;
import hn.gob.ine.listener.model.CalidadTraOcuOcupacion;
import hn.gob.ine.listener.model.CobCensistaProductividad;
import hn.gob.ine.listener.model.CobCensistaPorVivienda;
import hn.gob.ine.listener.model.IndicadoresControlNacional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class DestinoMonitoreoDAO {

    private static final Object LOCK_INDICADORES_CONTROL_NACIONAL = new Object();
    private static final Object LOCK_TOTAL_HOG_VIV = new Object();
    private static final Object LOCK_COB_CONDICION_VIVIENDA = new Object();
    private static final Object LOCK_COB_AREA = new Object();
    private static final Object LOCK_COB_EVOLUCION_COBERTURA = new Object();
    private static final Object LOCK_GE_VIV_COB_VIVIENDAS_CENSADAS = new Object();
    private static final Object LOCK_GE_VIV_COB_HOGARES_CENSADOS = new Object();
    private static final Object LOCK_GE_VIV_COB_TIPO_VIV = new Object();
    private static final Object LOCK_COB_TOTAL_VIVIENDAS_OCUPADAS = new Object();
    private static final Object LOCK_COB_TOTAL_VIVIENDAS_DESOCUPADA = new Object();
    private static final Object LOCK_COB_TOTAL_VIVIENDAS_OCUP_AUSENTES = new Object();
    private static final Object LOCK_COB_TOTAL_VIVIENDAS_PARTICULARES = new Object();
    private static final Object LOCK_GE_VIV_COB_CENSADAS_SIN = new Object();
    private static final Object LOCK_COB_COBERTURA_DEPARTAMENTOS = new Object();
    private static final Object LOCK_GE_VIV_COB_DEPTOS_COMPLETADOS = new Object();
    private static final Object LOCK_GE_VIV_COB_MUNIS_COMPLETADOS = new Object();
    private static final Object LOCK_CALIDAD_VIV_VIVIENDA = new Object();
    private static final Object LOCK_CALIDAD_VIV_HOGAR = new Object();
    private static final Object LOCK_CALIDAD_HOG_SERVICIOS = new Object();
    private static final Object LOCK_CALIDAD_HOG_NUCLEO = new Object();
    private static final Object LOCK_CALIDAD_HOG_HACINAMIENTO = new Object();
    private static final Object LOCK_CALIDAD_EDU_GRUPO_EDAD = new Object();
    private static final Object LOCK_CALIDAD_EDU_NIVEL_EDUCATIVO = new Object();
    private static final Object LOCK_CALIDAD_DISC_LIMITACION = new Object();
    private static final Object LOCK_CALIDAD_ETN_INDIGENA_AFRO = new Object();
    private static final Object LOCK_CALIDAD_MIG_EMIGRANTE_GENERO = new Object();
    private static final Object LOCK_CALIDAD_MIG_EMIGRANTE_PAIS_DEST = new Object();
    private static final Object LOCK_CALIDAD_MOR_MORTALIDAD = new Object();
    private static final Object LOCK_CALIDAD_FEC_FECUNDIDAD = new Object();
    private static final Object LOCK_CALIDAD_TRA_OCU_OCUPACION = new Object();
    private static final Object LOCK_CALIDAD_TIC_ACCESO = new Object();
    private static final Object LOCK_CALIDAD_NAC_RES_OTRO_LUGAR = new Object();
    private static final Object LOCK_CALIDAD_COMP_HOG_ENVEJECIMIENTO = new Object();
    private static final Object LOCK_CALIDAD_COMP_HOG_PIRAMIDE = new Object();
    private static final Object LOCK_CALIDAD_CONY_ESTADO = new Object();
    private static final Object LOCK_COB_CENSISTA_PRODUCTIVIDAD = new Object();
    private static final Object LOCK_COB_CENSISTA_POR_VIVIENDA = new Object();

    public void refrescarTotalHogViv(TotalHogViv total) throws Exception {

        synchronized (LOCK_TOTAL_HOG_VIV) {
            borrarTotalHogViv(total);
            insertarTotalHogViv(total);
        }
    }

    private void borrarTotalHogViv(TotalHogViv total) throws Exception {

        // OJO: la llave de borrado NO incluye zona/sector a proposito. El "ganador" de
        // zona/sector para un censista (obtenerCensistasPorDepartamento, rn=1) puede cambiar
        // de una corrida a otra segun sigan llegando estructuras nuevas -- si el DELETE
        // exigiera zona/sector exactos, la fila vieja (con el zona/sector de la corrida
        // anterior) nunca se borraria y quedarian dos filas duplicadas para el mismo
        // censista/segmento, sumando personas de mas (bug confirmado: 3,418 combos
        // duplicados encontrados en produccion).
        String sql
                = "DELETE FROM censo_monitoreo.total_hog_viv "
                + "WHERE depto = ? "
                + "AND muni = ? "
                + "AND segmento = ? "
                + "AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, total.getDepto());
            ps.setString(2, total.getMuni());
            ps.setString(3, total.getSegmento());
            ps.setString(4, total.getCensista());

            ps.executeUpdate();
        }
    }

    private void insertarTotalHogViv(TotalHogViv total) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.total_hog_viv "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, hogar, vivienda, cantidad_personas) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, total.getDepto());
            ps.setString(2, total.getMuni());
            ps.setInt(3, total.getApoyoMunicipal());
            ps.setInt(4, total.getZona());
            ps.setInt(5, total.getSector());
            ps.setString(6, total.getSegmento());
            ps.setString(7, total.getCensista());
            ps.setInt(8, total.getHogar());
            ps.setInt(9, total.getVivienda());
            ps.setInt(10, total.getCantidadPersonas());

            ps.executeUpdate();
        }
    }

    public void refrescarCobCondicionVivienda(CobCondicionVivienda cob) throws Exception {

        synchronized (LOCK_COB_CONDICION_VIVIENDA) {
            borrarCobCondicionVivienda(cob);
            insertarCobCondicionVivienda(cob);
        }
    }

    private void borrarCobCondicionVivienda(CobCondicionVivienda cob) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.cob_condicion_vivienda "
                + "WHERE depto = ? "
                + "AND muni = ? "
                + "AND sector = ? "
                + "AND segmento = ? "
                + "AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cob.getDepto());
            ps.setString(2, cob.getMuni());
            ps.setString(3, cob.getSector());
            ps.setString(4, cob.getSegmento());
            ps.setString(5, cob.getCensista());

            ps.executeUpdate();
        }
    }

    private void insertarCobCondicionVivienda(CobCondicionVivienda cob) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.cob_condicion_vivienda "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, ocupada_presentes, ocupadas_presentes, rechazadas, pendientes) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cob.getDepto());
            ps.setString(2, cob.getMuni());
            ps.setInt(3, cob.getApoyoMunicipal());
            ps.setInt(4, cob.getZona());
            ps.setString(5, cob.getSector());
            ps.setString(6, cob.getSegmento());
            ps.setString(7, cob.getCensista());
            ps.setInt(8, cob.getOcupadaPresentes());
            ps.setInt(9, cob.getOcupadasPresentes());
            ps.setInt(10, cob.getRechazadas());
            ps.setInt(11, cob.getPendientes());

            ps.executeUpdate();
        }
    }

    public void refrescarCobArea(CobArea cob) throws Exception {

        synchronized (LOCK_COB_AREA) {
            borrarCobArea(cob);
            insertarCobArea(cob);
        }
    }

    private void borrarCobArea(CobArea cob) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.cob_area "
                + "WHERE depto = ? "
                + "AND muni = ? "
                + "AND sector = ? "
                + "AND segmento = ? "
                + "AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cob.getDepto());
            ps.setString(2, cob.getMuni());
            ps.setString(3, cob.getSector());
            ps.setString(4, cob.getSegmento());
            ps.setString(5, cob.getCensista());

            ps.executeUpdate();
        }
    }

    private void insertarCobArea(CobArea cob) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.cob_area "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, area_urbana, area_rural) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cob.getDepto());
            ps.setString(2, cob.getMuni());
            ps.setInt(3, cob.getApoyoMunicipal());
            ps.setInt(4, cob.getZona());
            ps.setString(5, cob.getSector());
            ps.setString(6, cob.getSegmento());
            ps.setString(7, cob.getCensista());
            ps.setInt(8, cob.getAreaUrbana());
            ps.setInt(9, cob.getAreaRural());

            ps.executeUpdate();
        }
    }

    public void refrescarCobEvolucionCobertura(List<CobEvolucionCobertura> lista) throws Exception {

        if (lista == null || lista.isEmpty()) {
            return;
        }

        synchronized (LOCK_COB_EVOLUCION_COBERTURA) {
            borrarCobEvolucionCobertura(lista.get(0));

            for (CobEvolucionCobertura cob : lista) {
                insertarCobEvolucionCobertura(cob);
            }
        }
    }

    private void borrarCobEvolucionCobertura(CobEvolucionCobertura cob) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.cob_evolucion_cobertura "
                + "WHERE depto = ? "
                + "AND muni = ? "
                + "AND sector = ? "
                + "AND segmento = ? "
                + "AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cob.getDepto());
            ps.setString(2, cob.getMuni());
            ps.setString(3, cob.getSector());
            ps.setString(4, cob.getSegmento());
            ps.setString(5, cob.getCensista());

            ps.executeUpdate();
        }
    }

    private void insertarCobEvolucionCobertura(CobEvolucionCobertura cob) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.cob_evolucion_cobertura "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, avance_dia, viv_ocupadas_dia, fecha) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cob.getDepto());
            ps.setString(2, cob.getMuni());
            ps.setInt(3, cob.getApoyoMunicipal());
            ps.setInt(4, cob.getZona());
            ps.setString(5, cob.getSector());
            ps.setString(6, cob.getSegmento());
            ps.setString(7, cob.getCensista());
            ps.setInt(8, cob.getAvanceDia());
            ps.setInt(9, cob.getVivOcupadasDia());
            ps.setDate(10, cob.getFecha());

            ps.executeUpdate();
        }

    }

    public void refrescarGeVivCobViviendasCensadas(GeVivCobViviendasCensadas dato) throws Exception {

        synchronized (LOCK_GE_VIV_COB_VIVIENDAS_CENSADAS) {
            borrarGeVivCobViviendasCensadas(dato);
            insertarGeVivCobViviendasCensadas(dato);
        }
    }

    private void borrarGeVivCobViviendasCensadas(GeVivCobViviendasCensadas dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.`ge-viv-cob-viviendas-censadas` "
                + "WHERE depto = ? "
                + "AND mun = ? "
                + "AND sector = ? "
                + "AND segmento = ? "
                + "AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMun());
            ps.setString(3, dato.getSector());
            ps.setString(4, dato.getSegmento());
            ps.setString(5, dato.getCensista());

            ps.executeUpdate();
        }
    }

    private void insertarGeVivCobViviendasCensadas(GeVivCobViviendasCensadas dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.`ge-viv-cob-viviendas-censadas` "
                + "(depto, mun, apoyo_municipal, zona, sector, segmento, censista, cantidad_censadas) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMun());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setString(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantidadCensadas());

            ps.executeUpdate();
        }
    }

    public void refrescarGeVivCobHogaresCensados(GeVivCobHogaresCensados dato) throws Exception {

        synchronized (LOCK_GE_VIV_COB_HOGARES_CENSADOS) {
            borrarGeVivCobHogaresCensados(dato);
            insertarGeVivCobHogaresCensados(dato);
        }
    }

    private void borrarGeVivCobHogaresCensados(GeVivCobHogaresCensados dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.`ge-viv-cob-hogares-censados` "
                + "WHERE depto = ? "
                + "AND mun = ? "
                + "AND sector = ? "
                + "AND segmento = ? "
                + "AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMun());
            ps.setString(3, dato.getSector());
            ps.setString(4, dato.getSegmento());
            ps.setString(5, dato.getCensista());

            ps.executeUpdate();
        }
    }

    private void insertarGeVivCobHogaresCensados(GeVivCobHogaresCensados dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.`ge-viv-cob-hogares-censados` "
                + "(depto, mun, apoyo_municipal, zona, sector, segmento, censista, cantidad_censadas) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMun());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setString(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantidadCensadas());

            ps.executeUpdate();
        }
    }

    public void refrescarGeVivCobTipoViv(GeVivCobTipoViv dato) throws Exception {

        synchronized (LOCK_GE_VIV_COB_TIPO_VIV) {
            borrarGeVivCobTipoViv(dato);
            insertarGeVivCobTipoViv(dato);
        }
    }

    private void borrarGeVivCobTipoViv(GeVivCobTipoViv dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.`ge-viv-cob-tipo-viv` "
                + "WHERE depto = ? "
                + "AND muni = ? "
                + "AND sector = ? "
                + "AND segmento = ? "
                + "AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSector());
            ps.setString(4, dato.getSegmento());
            ps.setString(5, dato.getCensista());

            ps.executeUpdate();
        }
    }

    private void insertarGeVivCobTipoViv(GeVivCobTipoViv dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.`ge-viv-cob-tipo-viv` "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, viv_particular, apartamento, cuarteria, otro_tipo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setString(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getVivParticular());
            ps.setInt(9, dato.getApartamento());
            ps.setInt(10, dato.getCuarteria());
            ps.setInt(11, dato.getOtroTipo());

            ps.executeUpdate();
        }
    }

    public void refrescarCobTotalViviendasOcupadas(CobTotalViviendasOcupadas dato) throws Exception {

        synchronized (LOCK_COB_TOTAL_VIVIENDAS_OCUPADAS) {
            borrarCobTotalViviendasOcupadas(dato);
            insertarCobTotalViviendasOcupadas(dato);
        }
    }

    private void borrarCobTotalViviendasOcupadas(CobTotalViviendasOcupadas dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.cob_total_viviendas_ocupadas "
                + "WHERE depto = ? "
                + "AND muni = ? "
                + "AND apoyo_municipal = ? "
                                + "AND segmento = ? "
                + "AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setString(4, dato.getSegmento());
            ps.setString(5, dato.getCensista());

            ps.executeUpdate();
        }
    }

    private void insertarCobTotalViviendasOcupadas(CobTotalViviendasOcupadas dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.cob_total_viviendas_ocupadas "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cantidad_ocupadas, cantidad_censadas_boleta, cantidad_ocup_deso_rechazo, cantidad) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setString(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantidadOcupadas());
            ps.setInt(9, dato.getCantidadCensadasBoleta());
            ps.setInt(10, dato.getCantidadOcupDesoRechazo());
            ps.setInt(11, dato.getCantidad());

            ps.executeUpdate();
        }
    }

    public void refrescarCobTotalViviendasDesocupada(CobTotalViviendasDesocupada dato) throws Exception {

        synchronized (LOCK_COB_TOTAL_VIVIENDAS_DESOCUPADA) {
            borrarCobTotalViviendasDesocupada(dato);
            insertarCobTotalViviendasDesocupada(dato);
        }
    }

    private void borrarCobTotalViviendasDesocupada(CobTotalViviendasDesocupada dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.cob_total_viviendas_desocupada "
                + "WHERE depto = ? "
                + "AND muni = ? "
                + "AND apoyo_municipal = ? "
                                + "AND segmento = ? "
                + "AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setString(4, dato.getSegmento());
            ps.setString(5, dato.getCensista());

            ps.executeUpdate();
        }
    }

    private void insertarCobTotalViviendasDesocupada(CobTotalViviendasDesocupada dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.cob_total_viviendas_desocupada "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cantidad_desocupada, cantidad) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setString(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantidadDesocupada());
            ps.setInt(9, dato.getCantidad());

            ps.executeUpdate();
        }
    }

    public void refrescarCobTotalViviendasOcupAusentes(CobTotalViviendasOcupAusentes dato) throws Exception {

        synchronized (LOCK_COB_TOTAL_VIVIENDAS_OCUP_AUSENTES) {
            borrarCobTotalViviendasOcupAusentes(dato);
            insertarCobTotalViviendasOcupAusentes(dato);
        }
    }

    private void borrarCobTotalViviendasOcupAusentes(CobTotalViviendasOcupAusentes dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.cob_total_viviendas_ocup_ausentes "
                + "WHERE depto = ? "
                + "AND muni = ? "
                + "AND apoyo_municipal = ? "
                                + "AND segmento = ? "
                + "AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setString(4, dato.getSegmento());
            ps.setString(5, dato.getCensista());

            ps.executeUpdate();
        }
    }

    private void insertarCobTotalViviendasOcupAusentes(CobTotalViviendasOcupAusentes dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.cob_total_viviendas_ocup_ausentes "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cantidad_ocupadas_ausente, cantidad) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setString(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantidadOcupadasAusente());
            ps.setInt(9, dato.getCantidad());

            ps.executeUpdate();
        }
    }

    public void refrescarCobTotalViviendasParticulares(CobTotalViviendasParticulares dato) throws Exception {

        synchronized (LOCK_COB_TOTAL_VIVIENDAS_PARTICULARES) {
            borrarCobTotalViviendasParticulares(dato);
            insertarCobTotalViviendasParticulares(dato);
        }
    }

    private void borrarCobTotalViviendasParticulares(CobTotalViviendasParticulares dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.cob_total_viviendas_particulares "
                + "WHERE depto = ? "
                + "AND muni = ? "
                + "AND sector = ? "
                + "AND segmento = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSector());
            ps.setString(4, dato.getSegmento());

            ps.executeUpdate();
        }
    }

    private void insertarCobTotalViviendasParticulares(CobTotalViviendasParticulares dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.cob_total_viviendas_particulares "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cantidad_particulares, cantidad) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setString(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantidadParticulares());
            ps.setInt(9, dato.getCantidad());

            ps.executeUpdate();
        }
    }

    public void refrescarGeVivCobCensadasSin(GeVivCobCensadasSin dato) throws Exception {

        synchronized (LOCK_GE_VIV_COB_CENSADAS_SIN) {
            borrarGeVivCobCensadasSin(dato);
            insertarGeVivCobCensadasSin(dato);
        }
    }

    private void borrarGeVivCobCensadasSin(GeVivCobCensadasSin dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.`ge-viv-cob-censadas-sin` "
                + "WHERE depto = ? "
                + "AND muni = ? "
                + "AND sector = ? "
                + "AND segmento = ? "
                + "AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSector());
            ps.setString(4, dato.getSegmento());
            ps.setString(5, dato.getCensista());

            ps.executeUpdate();
        }
    }

    private void insertarGeVivCobCensadasSin(GeVivCobCensadasSin dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.`ge-viv-cob-censadas-sin` "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, agua, energia, sanitario) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setString(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getAgua());
            ps.setInt(9, dato.getEnergia());
            ps.setInt(10, dato.getSanitario());

            ps.executeUpdate();
        }
    }

    public void refrescarCobCoberturaDepartamentos(CobCoberturaDepartamentos dato) throws Exception {

        synchronized (LOCK_COB_COBERTURA_DEPARTAMENTOS) {
            borrarCobCoberturaDepartamentos(dato);
            insertarCobCoberturaDepartamentos(dato);
        }
    }

    private void borrarCobCoberturaDepartamentos(CobCoberturaDepartamentos dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.cob_cobertura_departamentos "
                + "WHERE depto = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.executeUpdate();
        }
    }

    private void insertarCobCoberturaDepartamentos(CobCoberturaDepartamentos dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.cob_cobertura_departamentos "
                + "(depto, depto_name, realizado, por_realizar) "
                + "VALUES (?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getDeptoName());
            ps.setInt(3, dato.getRealizado());
            ps.setInt(4, dato.getPorRealizar());
            ps.executeUpdate();
        }
    }

    public void refrescarGeVivCobDeptosCompletados(GeVivCobDeptosCompletados dato) throws Exception {

        synchronized (LOCK_GE_VIV_COB_DEPTOS_COMPLETADOS) {
            borrarGeVivCobDeptosCompletados(dato);
            insertarGeVivCobDeptosCompletados(dato);
        }
    }

    private void borrarGeVivCobDeptosCompletados(GeVivCobDeptosCompletados dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.`ge-viv-cob-deptos-completados` "
                + "WHERE depto = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.executeUpdate();
        }
    }

    private void insertarGeVivCobDeptosCompletados(GeVivCobDeptosCompletados dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.`ge-viv-cob-deptos-completados` "
                + "(depto, completado) "
                + "VALUES (?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getCompletado());
            ps.executeUpdate();
        }
    }

    public void refrescarGeVivCobMunisCompletados(GeVivCobMunisCompletados dato) throws Exception {

        synchronized (LOCK_GE_VIV_COB_MUNIS_COMPLETADOS) {
            borrarGeVivCobMunisCompletados(dato);
            insertarGeVivCobMunisCompletados(dato);
        }
    }

    private void borrarGeVivCobMunisCompletados(GeVivCobMunisCompletados dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.`ge-viv-cob-munis-completados` "
                + "WHERE depto = ? AND muni = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.executeUpdate();
        }
    }

    private void insertarGeVivCobMunisCompletados(GeVivCobMunisCompletados dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.`ge-viv-cob-munis-completados` "
                + "(depto, muni, completado) "
                + "VALUES (?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getCompletado());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadVivVivienda(CalidadVivVivienda dato) throws Exception {

        synchronized (LOCK_CALIDAD_VIV_VIVIENDA) {
            borrarCalidadVivVivienda(dato);
            insertarCalidadVivVivienda(dato);
        }
    }

    private void borrarCalidadVivVivienda(CalidadVivVivienda dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_viv_vivienda "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadVivVivienda(CalidadVivVivienda dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_viv_vivienda "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, tipo_casa_indep, tipo_apartamento, "
                + "tipo_anexo, tipo_cuarto_meson, tipo_no_construido, tipo_rancho, tipo_improvisado, "
                + "tipo_otro, piso_tierra, techo_teja, recibe_agua, cantidad) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getTipoCasaIndep());
            ps.setInt(9, dato.getTipoApartamento());
            ps.setInt(10, dato.getTipoAnexo());
            ps.setInt(11, dato.getTipoCuartoMeson());
            ps.setInt(12, dato.getTipoNoConstruido());
            ps.setInt(13, dato.getTipoRancho());
            ps.setInt(14, dato.getTipoImprovisado());
            ps.setInt(15, dato.getTipoOtro());
            ps.setInt(16, dato.getPisoTierra());
            ps.setInt(17, dato.getTechoTeja());
            ps.setInt(18, dato.getRecibeAgua());
            ps.setInt(19, dato.getCantidad());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadVivHogar(CalidadVivHogar dato) throws Exception {

        synchronized (LOCK_CALIDAD_VIV_HOGAR) {
            borrarCalidadVivHogar(dato);
            insertarCalidadVivHogar(dato);
        }
    }

    private void borrarCalidadVivHogar(CalidadVivHogar dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_viv_hogar "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadVivHogar(CalidadVivHogar dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_viv_hogar "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cantidad, electricidad, "
                + "internet, tipo_sanitario_alcant, tren_aseo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantidad());
            ps.setInt(9, dato.getElectricidad());
            ps.setInt(10, dato.getInternet());
            ps.setInt(11, dato.getTipoSanitarioAlcant());
            ps.setInt(12, dato.getTrenAseo());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadHogServicios(CalidadHogServicios dato) throws Exception {

        synchronized (LOCK_CALIDAD_HOG_SERVICIOS) {
            borrarCalidadHogServicios(dato);
            insertarCalidadHogServicios(dato);
        }
    }

    private void borrarCalidadHogServicios(CalidadHogServicios dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_hog_servicios "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadHogServicios(CalidadHogServicios dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_hog_servicios "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cantidad, refrigeradora, "
                + "televisor, computadora, celular, internet) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantidad());
            ps.setInt(9, dato.getRefrigeradora());
            ps.setInt(10, dato.getTelevisor());
            ps.setInt(11, dato.getComputadora());
            ps.setInt(12, dato.getCelular());
            ps.setInt(13, dato.getInternet());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadHogNucleo(CalidadHogNucleo dato) throws Exception {

        synchronized (LOCK_CALIDAD_HOG_NUCLEO) {
            borrarCalidadHogNucleo(dato);
            insertarCalidadHogNucleo(dato);
        }
    }

    private void borrarCalidadHogNucleo(CalidadHogNucleo dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_hog_nucleo "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadHogNucleo(CalidadHogNucleo dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_hog_nucleo "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, jefe_mujer, jefe_hombre, "
                + "biparental_sin_hijos, biparental_con_hijos, unipersonal, monoparental_jefe_mujer) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getJefeMujer());
            ps.setInt(9, dato.getJefeHombre());
            ps.setInt(10, dato.getBiparentalSinHijos());
            ps.setInt(11, dato.getBiparentalConHijos());
            ps.setInt(12, dato.getUnipersonal());
            ps.setInt(13, dato.getMonoparentalJefeMujer());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadHogHacinamiento(CalidadHogHacinamiento dato) throws Exception {

        synchronized (LOCK_CALIDAD_HOG_HACINAMIENTO) {
            borrarCalidadHogHacinamiento(dato);
            insertarCalidadHogHacinamiento(dato);
        }
    }

    private void borrarCalidadHogHacinamiento(CalidadHogHacinamiento dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_hog_hacinamiento "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadHogHacinamiento(CalidadHogHacinamiento dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_hog_hacinamiento "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, mayor_3_hac, num_personas) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getMayor3Hac());
            ps.setInt(9, dato.getNumPersonas());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadEduGrupoEdad(CalidadEduGrupoEdad dato) throws Exception {

        synchronized (LOCK_CALIDAD_EDU_GRUPO_EDAD) {
            borrarCalidadEduGrupoEdad(dato);
            insertarCalidadEduGrupoEdad(dato);
        }
    }

    private void borrarCalidadEduGrupoEdad(CalidadEduGrupoEdad dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_edu_grupo_edad "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadEduGrupoEdad(CalidadEduGrupoEdad dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_edu_grupo_edad "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, edu_edad_5_6, edu_edad_5_12, "
                + "edu_edad_5_17, edu_edad_6_12, edu_edad_7_12, edu_edad_13_17, poblacion_6_12, "
                + "poblacion_7_12, poblacion_5_6, poblacion_13_17, poblacion_5_17, edu_edad_3_mas, poblacion_3_mas) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getEduEdad5_6());
            ps.setInt(9, dato.getEduEdad5_12());
            ps.setInt(10, dato.getEduEdad5_17());
            ps.setInt(11, dato.getEduEdad6_12());
            ps.setInt(12, dato.getEduEdad7_12());
            ps.setInt(13, dato.getEduEdad13_17());
            ps.setInt(14, dato.getPoblacion6_12());
            ps.setInt(15, dato.getPoblacion7_12());
            ps.setInt(16, dato.getPoblacion5_6());
            ps.setInt(17, dato.getPoblacion13_17());
            ps.setInt(18, dato.getPoblacion5_17());
            ps.setInt(19, dato.getEduEdad3Mas());
            ps.setInt(20, dato.getPoblacion3Mas());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadEduNivelEducativo(CalidadEduNivelEducativo dato) throws Exception {

        synchronized (LOCK_CALIDAD_EDU_NIVEL_EDUCATIVO) {
            borrarCalidadEduNivelEducativo(dato);
            insertarCalidadEduNivelEducativo(dato);
        }
    }

    private void borrarCalidadEduNivelEducativo(CalidadEduNivelEducativo dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_edu_nivel_educativo "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadEduNivelEducativo(CalidadEduNivelEducativo dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_edu_nivel_educativo "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cant_no_educ, cant_alfabetizacion, "
                + "cant_prebasica, cant_basica, cant_media, cant_tec_superior, cant_tec_no_superior, "
                + "cant_universidad, cant_especialidad, cant_maestria, cant_doctorado, cant_analfabeta, "
                + "poblacion_15_mas, total_anios_estudio, poblacion_para_promedio, poblacion_nivel_educativo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantNoEduc());
            ps.setInt(9, dato.getCantAlfabetizacion());
            ps.setInt(10, dato.getCantPrebasica());
            ps.setInt(11, dato.getCantBasica());
            ps.setInt(12, dato.getCantMedia());
            ps.setInt(13, dato.getCantTecSuperior());
            ps.setInt(14, dato.getCantTecNoSuperior());
            ps.setInt(15, dato.getCantUniversidad());
            ps.setInt(16, dato.getCantEspecialidad());
            ps.setInt(17, dato.getCantMaestria());
            ps.setInt(18, dato.getCantDoctorado());
            ps.setInt(19, dato.getCantAnalfabeta());
            ps.setInt(20, dato.getPoblacion15Mas());
            ps.setInt(21, dato.getTotalAniosEstudio());
            ps.setInt(22, dato.getPoblacionParaPromedio());
            ps.setInt(23, dato.getPoblacionNivelEducativo());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadDiscLimitacion(CalidadDiscLimitacion dato) throws Exception {

        synchronized (LOCK_CALIDAD_DISC_LIMITACION) {
            borrarCalidadDiscLimitacion(dato);
            insertarCalidadDiscLimitacion(dato);
        }
    }

    private void borrarCalidadDiscLimitacion(CalidadDiscLimitacion dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_disc_limitacion "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadDiscLimitacion(CalidadDiscLimitacion dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_disc_limitacion "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cant_limitacion, cant_lim_caminar, "
                + "cant_lim_habla, cant_lim_vision, cant_lim_oir, cant_lim_aprender, cant_lim_brazo, "
                + "cant_lim_valerse, cant_sin_limitacion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantLimitacion());
            ps.setInt(9, dato.getCantLimCaminar());
            ps.setInt(10, dato.getCantLimHabla());
            ps.setInt(11, dato.getCantLimVision());
            ps.setInt(12, dato.getCantLimOir());
            ps.setInt(13, dato.getCantLimAprender());
            ps.setInt(14, dato.getCantLimBrazo());
            ps.setInt(15, dato.getCantLimValerse());
            ps.setInt(16, dato.getCantSinLimitacion());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadEtnIndigenaAfro(CalidadEtnIndigenaAfro dato) throws Exception {

        synchronized (LOCK_CALIDAD_ETN_INDIGENA_AFRO) {
            borrarCalidadEtnIndigenaAfro(dato);
            insertarCalidadEtnIndigenaAfro(dato);
        }
    }

    private void borrarCalidadEtnIndigenaAfro(CalidadEtnIndigenaAfro dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_etn_indigena_afro "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadEtnIndigenaAfro(CalidadEtnIndigenaAfro dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_etn_indigena_afro "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cant_mujer, cant_hombre, "
                + "cant_indigena, cant_afro, cant_indigena_h, cant_indigena_m, cant_afro_h, cant_afro_m) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantMujer());
            ps.setInt(9, dato.getCantHombre());
            ps.setInt(10, dato.getCantIndigena());
            ps.setInt(11, dato.getCantAfro());
            ps.setInt(12, dato.getCantIndigenaH());
            ps.setInt(13, dato.getCantIndigenaM());
            ps.setInt(14, dato.getCantAfroH());
            ps.setInt(15, dato.getCantAfroM());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadMigEmigranteGenero(CalidadMigEmigranteGenero dato) throws Exception {

        synchronized (LOCK_CALIDAD_MIG_EMIGRANTE_GENERO) {
            borrarCalidadMigEmigranteGenero(dato);
            insertarCalidadMigEmigranteGenero(dato);
        }
    }

    private void borrarCalidadMigEmigranteGenero(CalidadMigEmigranteGenero dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_mig_emigrante_genero "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadMigEmigranteGenero(CalidadMigEmigranteGenero dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_mig_emigrante_genero "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cant_mujer_emi, cant_hombre_emi, "
                + "edad_0_4_h, edad_0_4_m, edad_5_9_h, edad_5_9_m, edad_10_14_h, edad_10_14_m, "
                + "edad_15_19_h, edad_15_19_m, edad_20_24_h, edad_20_24_m, edad_25_29_h, edad_25_29_m, "
                + "edad_30_34_h, edad_30_34_m, edad_35_39_h, edad_35_39_m, edad_40_44_h, edad_40_44_m, "
                + "edad_45_49_h, edad_45_49_m, edad_50_54_h, edad_50_54_m, edad_55_59_h, edad_55_59_m, "
                + "edad_60_64_h, edad_60_64_m, edad_65_69_h, edad_65_69_m, edad_70_74_h, edad_70_74_m, "
                + "edad_75_79_h, edad_75_79_m, edad_80_mas_h, edad_80_mas_m, hogares_con_emigrante) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantMujerEmi());
            ps.setInt(9, dato.getCantHombreEmi());
            ps.setInt(10, dato.getEdad0_4H());
            ps.setInt(11, dato.getEdad0_4M());
            ps.setInt(12, dato.getEdad5_9H());
            ps.setInt(13, dato.getEdad5_9M());
            ps.setInt(14, dato.getEdad10_14H());
            ps.setInt(15, dato.getEdad10_14M());
            ps.setInt(16, dato.getEdad15_19H());
            ps.setInt(17, dato.getEdad15_19M());
            ps.setInt(18, dato.getEdad20_24H());
            ps.setInt(19, dato.getEdad20_24M());
            ps.setInt(20, dato.getEdad25_29H());
            ps.setInt(21, dato.getEdad25_29M());
            ps.setInt(22, dato.getEdad30_34H());
            ps.setInt(23, dato.getEdad30_34M());
            ps.setInt(24, dato.getEdad35_39H());
            ps.setInt(25, dato.getEdad35_39M());
            ps.setInt(26, dato.getEdad40_44H());
            ps.setInt(27, dato.getEdad40_44M());
            ps.setInt(28, dato.getEdad45_49H());
            ps.setInt(29, dato.getEdad45_49M());
            ps.setInt(30, dato.getEdad50_54H());
            ps.setInt(31, dato.getEdad50_54M());
            ps.setInt(32, dato.getEdad55_59H());
            ps.setInt(33, dato.getEdad55_59M());
            ps.setInt(34, dato.getEdad60_64H());
            ps.setInt(35, dato.getEdad60_64M());
            ps.setInt(36, dato.getEdad65_69H());
            ps.setInt(37, dato.getEdad65_69M());
            ps.setInt(38, dato.getEdad70_74H());
            ps.setInt(39, dato.getEdad70_74M());
            ps.setInt(40, dato.getEdad75_79H());
            ps.setInt(41, dato.getEdad75_79M());
            ps.setInt(42, dato.getEdad80MasH());
            ps.setInt(43, dato.getEdad80MasM());
            ps.setInt(44, dato.getHogaresConEmigrante());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadMigEmigrantePaisDest(CalidadMigEmigrantePaisDest dato) throws Exception {

        synchronized (LOCK_CALIDAD_MIG_EMIGRANTE_PAIS_DEST) {
            borrarCalidadMigEmigrantePaisDest(dato);
            insertarCalidadMigEmigrantePaisDest(dato);
        }
    }

    private void borrarCalidadMigEmigrantePaisDest(CalidadMigEmigrantePaisDest dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_mig_emigrante_pais_dest "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadMigEmigrantePaisDest(CalidadMigEmigrantePaisDest dato) throws Exception {

        // NOTA: pais_1, pais_2, pais_3, pais_4 y pais_otro (totales sin desglose por sexo) NO
        // se insertan porque esas columnas no existen en la tabla destino -son redundantes con
        // pais_eeuu_h+pais_eeuu_m, pais_espana_h+pais_espana_m, etc., que si se insertan abajo.
        // Antes de este fix esta consulta fallaba con "Unknown column 'pais_1' in 'INSERT INTO'"
        // en el 100% de los censistas (la tabla tenia 0 filas).
        String sql
                = "INSERT INTO censo_monitoreo.calidad_mig_emigrante_pais_dest "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cant_mujer, cant_hombre, "
                + "cantidad, "
                + "pais_eeuu_h, pais_eeuu_m, pais_espana_h, pais_espana_m, "
                + "pais_mexico_h, pais_mexico_m, pais_canada_h, pais_canada_m, "
                + "pais_italia_h, pais_italia_m, pais_otro_h, pais_otro_m) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantMujer());
            ps.setInt(9, dato.getCantHombre());
            ps.setInt(10, dato.getCantidad());
            ps.setInt(11, dato.getPaisEeuuH());
            ps.setInt(12, dato.getPaisEeuuM());
            ps.setInt(13, dato.getPaisEspanaH());
            ps.setInt(14, dato.getPaisEspanaM());
            ps.setInt(15, dato.getPaisMexicoH());
            ps.setInt(16, dato.getPaisMexicoM());
            ps.setInt(17, dato.getPaisCanadaH());
            ps.setInt(18, dato.getPaisCanadaM());
            ps.setInt(19, dato.getPaisItaliaH());
            ps.setInt(20, dato.getPaisItaliaM());
            ps.setInt(21, dato.getPaisOtroH());
            ps.setInt(22, dato.getPaisOtroM());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadMorMortalidad(CalidadMorMortalidad dato) throws Exception {

        synchronized (LOCK_CALIDAD_MOR_MORTALIDAD) {
            borrarCalidadMorMortalidad(dato);
            insertarCalidadMorMortalidad(dato);
        }
    }

    private void borrarCalidadMorMortalidad(CalidadMorMortalidad dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_mor_mortalidad "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadMorMortalidad(CalidadMorMortalidad dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_mor_mortalidad "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cant_hombre, cant_mujer, "
                + "edad_0_h, edad_0_m, edad_1_4_h, edad_1_4_m, edad_5_14_h, edad_5_14_m, "
                + "edad_15_24_h, edad_15_24_m, edad_25_34_h, edad_25_34_m, edad_35_44_h, edad_35_44_m, "
                + "edad_45_54_h, edad_45_54_m, edad_55_64_h, edad_55_64_m, edad_65_74_h, edad_65_74_m, "
                + "edad_75_mas_h, edad_75_mas_m, cantidad, hogar_con_un_fallecido) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantHombre());
            ps.setInt(9, dato.getCantMujer());
            ps.setInt(10, dato.getEdad0H());
            ps.setInt(11, dato.getEdad0M());
            ps.setInt(12, dato.getEdad1_4H());
            ps.setInt(13, dato.getEdad1_4M());
            ps.setInt(14, dato.getEdad5_14H());
            ps.setInt(15, dato.getEdad5_14M());
            ps.setInt(16, dato.getEdad15_24H());
            ps.setInt(17, dato.getEdad15_24M());
            ps.setInt(18, dato.getEdad25_34H());
            ps.setInt(19, dato.getEdad25_34M());
            ps.setInt(20, dato.getEdad35_44H());
            ps.setInt(21, dato.getEdad35_44M());
            ps.setInt(22, dato.getEdad45_54H());
            ps.setInt(23, dato.getEdad45_54M());
            ps.setInt(24, dato.getEdad55_64H());
            ps.setInt(25, dato.getEdad55_64M());
            ps.setInt(26, dato.getEdad65_74H());
            ps.setInt(27, dato.getEdad65_74M());
            ps.setInt(28, dato.getEdad75MasH());
            ps.setInt(29, dato.getEdad75MasM());
            ps.setInt(30, dato.getCantidad());
            ps.setInt(31, dato.getHogarConUnFallecido());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadFecFecundidad(CalidadFecFecundidad dato) throws Exception {

        synchronized (LOCK_CALIDAD_FEC_FECUNDIDAD) {
            borrarCalidadFecFecundidad(dato);
            insertarCalidadFecFecundidad(dato);
        }
    }

    private void borrarCalidadFecFecundidad(CalidadFecFecundidad dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_fec_fecundidad "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadFecFecundidad(CalidadFecFecundidad dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_fec_fecundidad "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cant_mujeres_15_49_anios, cant_mujer_15_con_hijos, "
                + "cant_hijos_nacidos_15_49, cant_hijos_nacidos, edad_15_19, edad_20_24, edad_25_29, "
                + "edad_30_34, edad_35_39, edad_40_44, edad_45_49, "
                + "`1er_hijo_15_19`, `1er_hijo_20_24`, `1er_hijo_25_29`, `1er_hijo_30_34`, "
                + "`1er_hijo_35_39`, `1er_hijo_40_44`, `1er_hijo_45_49`, "
                + "total_mujeres, total_mujeres_15_mas, "
                + "hijos_15_19, hijos_20_24, hijos_25_29, hijos_30_34, hijos_35_39, hijos_40_44, hijos_45_49) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantMujeres15_49Anios());
            ps.setInt(9, dato.getCantMujer15ConHijos());
            ps.setInt(10, dato.getCantHijosNacidos15_49());
            ps.setInt(11, dato.getCantHijosNacidos());
            ps.setInt(12, dato.getEdad15_19());
            ps.setInt(13, dato.getEdad20_24());
            ps.setInt(14, dato.getEdad25_29());
            ps.setInt(15, dato.getEdad30_34());
            ps.setInt(16, dato.getEdad35_39());
            ps.setInt(17, dato.getEdad40_44());
            ps.setInt(18, dato.getEdad45_49());
            ps.setInt(19, dato.getPrimerHijo15_19());
            ps.setInt(20, dato.getPrimerHijo20_24());
            ps.setInt(21, dato.getPrimerHijo25_29());
            ps.setInt(22, dato.getPrimerHijo30_34());
            ps.setInt(23, dato.getPrimerHijo35_39());
            ps.setInt(24, dato.getPrimerHijo40_44());
            ps.setInt(25, dato.getPrimerHijo45_49());
            ps.setInt(26, dato.getTotalMujeres());
            ps.setInt(27, dato.getTotalMujeres15Mas());
            ps.setInt(28, dato.getHijos15_19());
            ps.setInt(29, dato.getHijos20_24());
            ps.setInt(30, dato.getHijos25_29());
            ps.setInt(31, dato.getHijos30_34());
            ps.setInt(32, dato.getHijos35_39());
            ps.setInt(33, dato.getHijos40_44());
            ps.setInt(34, dato.getHijos45_49());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadTraOcuOcupacion(CalidadTraOcuOcupacion dato) throws Exception {

        synchronized (LOCK_CALIDAD_TRA_OCU_OCUPACION) {
            borrarCalidadTraOcuOcupacion(dato);
            insertarCalidadTraOcuOcupacion(dato);
        }
    }

    private void borrarCalidadTraOcuOcupacion(CalidadTraOcuOcupacion dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_tra_ocu_ocupacion "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadTraOcuOcupacion(CalidadTraOcuOcupacion dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_tra_ocu_ocupacion "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cant_hombre, cant_mujer, "
                + "cant_p_ocupada_m, cant_p_ocupada_h, cant_p_desocupada_m, cant_p_desocupada_h, "
                + "cant_retirada_edad_15_m, cant_retirada_edad_15_h, cant_cuidado_hogar_m, cant_cuidado_hogar_h, "
                + "cant_sector_publico_m, cant_sector_publico_h, cant_sector_privado_m, cant_sector_privado_h, "
                + "cant_empleo_domestico_m, cant_empleo_domestico_h, poblacion_15_mas_m, poblacion_15_mas_h) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantHombre());
            ps.setInt(9, dato.getCantMujer());
            ps.setInt(10, dato.getCantPOcupadaM());
            ps.setInt(11, dato.getCantPOcupadaH());
            ps.setInt(12, dato.getCantPDesocupadaM());
            ps.setInt(13, dato.getCantPDesocupadaH());
            ps.setInt(14, dato.getCantRetiradaEdad15M());
            ps.setInt(15, dato.getCantRetiradaEdad15H());
            ps.setInt(16, dato.getCantCuidadoHogarM());
            ps.setInt(17, dato.getCantCuidadoHogarH());
            ps.setInt(18, dato.getCantSectorPublicoM());
            ps.setInt(19, dato.getCantSectorPublicoH());
            ps.setInt(20, dato.getCantSectorPrivadoM());
            ps.setInt(21, dato.getCantSectorPrivadoH());
            ps.setInt(22, dato.getCantEmpleoDomesticoM());
            ps.setInt(23, dato.getCantEmpleoDomesticoH());
            ps.setInt(24, dato.getPoblacion15MasM());
            ps.setInt(25, dato.getPoblacion15MasH());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadTicAcceso(CalidadTicAcceso dato) throws Exception {

        synchronized (LOCK_CALIDAD_TIC_ACCESO) {
            borrarCalidadTicAcceso(dato);
            insertarCalidadTicAcceso(dato);
        }
    }

    private void borrarCalidadTicAcceso(CalidadTicAcceso dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_tic_acceso "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadTicAcceso(CalidadTicAcceso dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_tic_acceso "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cant_una_tic_minimo, cant_con_compu, "
                + "cant_con_tablet, cant_con_celular, cant_con_tel_fijo, cant_tiene_internet, poblacion_total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantUnaTicMinimo());
            ps.setInt(9, dato.getCantConCompu());
            ps.setInt(10, dato.getCantConTablet());
            ps.setInt(11, dato.getCantConCelular());
            ps.setInt(12, dato.getCantConTelFijo());
            ps.setInt(13, dato.getCantTieneInternet());
            ps.setInt(14, dato.getPoblacionTotal());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadNacResOtroLugar(CalidadNacResOtroLugar dato) throws Exception {

        synchronized (LOCK_CALIDAD_NAC_RES_OTRO_LUGAR) {
            borrarCalidadNacResOtroLugar(dato);
            insertarCalidadNacResOtroLugar(dato);
        }
    }

    private void borrarCalidadNacResOtroLugar(CalidadNacResOtroLugar dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_nac_res_otro_lugar "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadNacResOtroLugar(CalidadNacResOtroLugar dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_nac_res_otro_lugar "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cant_nac_otro_muni, cant_nac_otro_pais, "
                + "cant_nac_5anios, cant_nac_otro_muni_h, cant_nac_otro_muni_m, cant_nac_otro_pais_h, "
                + "cant_nac_otro_pais_m, cant_nac_5anios_h, cant_nac_5anios_m, cantidad, cant_5_mas) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantNacOtroMuni());
            ps.setInt(9, dato.getCantNacOtroPais());
            ps.setInt(10, dato.getCantNac5anios());
            ps.setInt(11, dato.getCantNacOtroMuniH());
            ps.setInt(12, dato.getCantNacOtroMuniM());
            ps.setInt(13, dato.getCantNacOtroPaisH());
            ps.setInt(14, dato.getCantNacOtroPaisM());
            ps.setInt(15, dato.getCantNac5aniosH());
            ps.setInt(16, dato.getCantNac5aniosM());
            ps.setInt(17, dato.getCantidad());
            ps.setInt(18, dato.getCant5Mas());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadCompHogEnvejecimiento(CalidadCompHogEnvejecimiento dato) throws Exception {

        synchronized (LOCK_CALIDAD_COMP_HOG_ENVEJECIMIENTO) {
            borrarCalidadCompHogEnvejecimiento(dato);
            insertarCalidadCompHogEnvejecimiento(dato);
        }
    }

    private void borrarCalidadCompHogEnvejecimiento(CalidadCompHogEnvejecimiento dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_comp_hog_envejecimiento "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadCompHogEnvejecimiento(CalidadCompHogEnvejecimiento dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_comp_hog_envejecimiento "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, no_inscrita_rnp_h, no_inscrita_rnp_m, "
                + "cant_mayor_60_h, cant_mayor_60_m, cant_hombre, cant_mujer, cant_100_mas_h, cant_100_mas_m, "
                + "cantidad, cant_menor_15_h, cant_menor_15_m) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getNoInscritaRnpH());
            ps.setInt(9, dato.getNoInscritaRnpM());
            ps.setInt(10, dato.getCantMayor60H());
            ps.setInt(11, dato.getCantMayor60M());
            ps.setInt(12, dato.getCantHombre());
            ps.setInt(13, dato.getCantMujer());
            ps.setInt(14, dato.getCant100MasH());
            ps.setInt(15, dato.getCant100MasM());
            ps.setInt(16, dato.getCantidad());
            ps.setInt(17, dato.getCantMenor15H());
            ps.setInt(18, dato.getCantMenor15M());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadCompHogPiramide(CalidadCompHogPiramide dato) throws Exception {

        synchronized (LOCK_CALIDAD_COMP_HOG_PIRAMIDE) {
            borrarCalidadCompHogPiramide(dato);
            insertarCalidadCompHogPiramide(dato);
        }
    }

    private void borrarCalidadCompHogPiramide(CalidadCompHogPiramide dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_comp_hog_piramide "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadCompHogPiramide(CalidadCompHogPiramide dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_comp_hog_piramide "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, edad_70_74_h, edad_70_74_m, "
                + "edad_75_79_h, edad_75_79_m, edad_80_84_h, edad_80_84_m, edad_85_89_h, edad_85_89_m, "
                + "edad_90_94_h, edad_90_94_m, edad_95_99_h, edad_95_99_m, edad_100_mas_h, edad_100_mas_m) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getEdad70_74H());
            ps.setInt(9, dato.getEdad70_74M());
            ps.setInt(10, dato.getEdad75_79H());
            ps.setInt(11, dato.getEdad75_79M());
            ps.setInt(12, dato.getEdad80_84H());
            ps.setInt(13, dato.getEdad80_84M());
            ps.setInt(14, dato.getEdad85_89H());
            ps.setInt(15, dato.getEdad85_89M());
            ps.setInt(16, dato.getEdad90_94H());
            ps.setInt(17, dato.getEdad90_94M());
            ps.setInt(18, dato.getEdad95_99H());
            ps.setInt(19, dato.getEdad95_99M());
            ps.setInt(20, dato.getEdad100MasH());
            ps.setInt(21, dato.getEdad100MasM());
            ps.executeUpdate();
        }
    }

    public void refrescarCalidadConyEstado(CalidadConyEstado dato) throws Exception {

        synchronized (LOCK_CALIDAD_CONY_ESTADO) {
            borrarCalidadConyEstado(dato);
            insertarCalidadConyEstado(dato);
        }
    }

    private void borrarCalidadConyEstado(CalidadConyEstado dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.calidad_cony_estado "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCalidadConyEstado(CalidadConyEstado dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.calidad_cony_estado "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, cant_mujer, cant_hombre, "
                + "cant_casado_h, cant_casado_m, cant_union_libre_h, cant_union_libre_m, "
                + "cant_separado_h, cant_separado_m, cant_divorciado_h, cant_divorciado_m, "
                + "cant_viudo_h, cant_viudo_m, cant_soltero_h, cant_soltero_m, "
                + "menor_18_casado_h, menor_18_casado_m, menor_18_union_libre_h, menor_18_union_libre_m, "
                + "menor_18_separado_h, menor_18_separado_m, menor_18_divorciado_h, menor_18_divorciado_m, "
                + "edad_menos_18_h, edad_menos_18_m) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getCantMujer());
            ps.setInt(9, dato.getCantHombre());
            ps.setInt(10, dato.getCantCasadoH());
            ps.setInt(11, dato.getCantCasadoM());
            ps.setInt(12, dato.getCantUnionLibreH());
            ps.setInt(13, dato.getCantUnionLibreM());
            ps.setInt(14, dato.getCantSeparadoH());
            ps.setInt(15, dato.getCantSeparadoM());
            ps.setInt(16, dato.getCantDivorciadoH());
            ps.setInt(17, dato.getCantDivorciadoM());
            ps.setInt(18, dato.getCantViudoH());
            ps.setInt(19, dato.getCantViudoM());
            ps.setInt(20, dato.getCantSolteroH());
            ps.setInt(21, dato.getCantSolteroM());
            ps.setInt(22, dato.getMenor18CasadoH());
            ps.setInt(23, dato.getMenor18CasadoM());
            ps.setInt(24, dato.getMenor18UnionLibreH());
            ps.setInt(25, dato.getMenor18UnionLibreM());
            ps.setInt(26, dato.getMenor18SeparadoH());
            ps.setInt(27, dato.getMenor18SeparadoM());
            ps.setInt(28, dato.getMenor18DivorciadoH());
            ps.setInt(29, dato.getMenor18DivorciadoM());
            ps.setInt(30, dato.getEdadMenos18H());
            ps.setInt(31, dato.getEdadMenos18M());
            ps.executeUpdate();
        }
    }

    public void refrescarCobCensistaProductividad(CobCensistaProductividad dato) throws Exception {

        synchronized (LOCK_COB_CENSISTA_PRODUCTIVIDAD) {
            borrarCobCensistaProductividad(dato);
            insertarCobCensistaProductividad(dato);
        }
    }

    private void borrarCobCensistaProductividad(CobCensistaProductividad dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.cob_censista_productividad "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCobCensistaProductividad(CobCensistaProductividad dato) throws Exception {

        int estructurasAsignadas = 0;
        int estructurasPendientes = 0;
        Double pctAvance = null;

        // estructuras_asignadas: viene del catalogo de segmentos (sectores_segmentos_completos),
        // no de cnpv_data, por eso se consulta aqui contra censo_monitoreo (destino).
        String sqlAsignaciones
                = "SELECT cantidad_asignaciones "
                + "FROM censo_monitoreo.sectores_segmentos_completos "
                + "WHERE depto = ? AND muni = ? AND segmento = ? "
                + "LIMIT 1";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sqlAsignaciones)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    estructurasAsignadas = rs.getInt("cantidad_asignaciones");
                }
            }
        }

        estructurasPendientes = Math.max(estructurasAsignadas - dato.getEstructurasTrabajadas(), 0);

        // pct_avance: formula oficial del Manual del Tecnico en Monitoreo (pag. 10-11):
        // (avance real acumulado % / avance esperado acumulado %) * 100,
        // donde avance esperado acumulado = dias_trabajados * 2.5% (tope 100%).
        String sqlEsperadas
                = "SELECT SUM(cantidad_esperada) AS esperado_total "
                + "FROM censo_monitoreo.cob_total_viviendas_esperadas "
                + "WHERE depto = ? AND muni = ? AND segmento = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sqlEsperadas)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int esperadoTotal = rs.getInt("esperado_total");
                    if (esperadoTotal > 0) {
                        int avanceTotal = dato.getViviendasDesocupadas()
                                + dato.getViviendasOcupAusentes()
                                + dato.getCuestionariosEfectivos();
                        double avancePct = avanceTotal * 100.0 / esperadoTotal;
                        double esperadoPctHoy = Math.min(dato.getDiasTrabajados() * 2.5, 100.0);
                        if (esperadoPctHoy > 0) {
                            pctAvance = avancePct / esperadoPctHoy * 100.0;
                        }
                    }
                }
            }
        }

        // estado_semaforo: mismos rangos del "Tablero de alerta" del Manual del Tecnico en Monitoreo (fig. 3, pag. 10).
        String estadoSemaforo = calcularEstadoSemaforo(pctAvance);

        String sql
                = "INSERT INTO censo_monitoreo.cob_censista_productividad "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, "
                + "estructuras_trabajadas, estructuras_nuevas, total_viviendas_particulares, viviendas_desocupadas, "
                + "viviendas_ocup_ausentes, rechazos, transformadas, referencias, cuestionarios_efectivos, cantidad_visitas, "
                + "promedio_duracion_seg, max_duracion_seg, dias_trabajados, "
                + "cantidad_hogares_unipersonales, cantidad_hogares_mas_3, cantidad_hogares, cantidad_personas, area, "
                + "estructuras_asignadas, estructuras_pendientes, pct_avance, estado_semaforo) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getEstructurasTrabajadas());
            ps.setInt(9, dato.getEstructurasNuevas());
            ps.setInt(10, dato.getTotalViviendasParticulares());
            ps.setInt(11, dato.getViviendasDesocupadas());
            ps.setInt(12, dato.getViviendasOcupAusentes());
            ps.setInt(13, dato.getRechazos());
            ps.setInt(14, dato.getTransformadas());
            ps.setInt(15, dato.getReferencias());
            ps.setInt(16, dato.getCuestionariosEfectivos());
            ps.setInt(17, dato.getCantidadVisitas());
            ps.setInt(18, dato.getPromedioDuracionSeg());
            ps.setInt(19, dato.getMaxDuracionSeg());
            ps.setInt(20, dato.getDiasTrabajados());
            ps.setInt(21, dato.getCantidadHogaresUnipersonales());
            ps.setInt(22, dato.getCantidadHogaresMas3());
            ps.setInt(23, dato.getCantidadHogares());
            ps.setInt(24, dato.getCantidadPersonas());
            ps.setInt(25, dato.getArea());
            ps.setInt(26, estructurasAsignadas);
            ps.setInt(27, estructurasPendientes);
            if (pctAvance == null) {
                ps.setNull(28, java.sql.Types.DECIMAL);
            } else {
                ps.setDouble(28, pctAvance);
            }
            ps.setString(29, estadoSemaforo);
            ps.executeUpdate();
        }
    }

    // Rangos del "Tablero de alerta" (Manual del Tecnico en Monitoreo, fig. 3, pag. 10).
    // Sin parentesis, %, < ni > a proposito: esos simbolos rompen filtros WHERE estado_semaforo = '...'
    // en reportes de la plataforma de reportes. Solo nombre + rango numerico plano.
    private String calcularEstadoSemaforo(Double pctAvance) {
        if (pctAvance == null || pctAvance == 0) {
            return "Blanco 0";
        } else if (pctAvance < 50) {
            return "Rojo menor 50";
        } else if (pctAvance < 85) {
            return "Amarillo Palido 50-85";
        } else if (pctAvance < 95) {
            return "Amarillo 85-95";
        } else if (pctAvance <= 105) {
            return "Verde 95-105";
        } else if (pctAvance <= 120) {
            return "Azul 105-120";
        } else {
            return "Anaranjado mayor 120";
        }
    }

    public void refrescarCobCensistaPorVivienda(CobCensistaPorVivienda dato) throws Exception {

        synchronized (LOCK_COB_CENSISTA_POR_VIVIENDA) {
            borrarCobCensistaPorVivienda(dato);
            insertarCobCensistaPorVivienda(dato);
        }
    }

    private void borrarCobCensistaPorVivienda(CobCensistaPorVivienda dato) throws Exception {

        String sql
                = "DELETE FROM censo_monitoreo.cob_censista_por_vivienda "
                + "WHERE depto = ? AND muni = ? AND segmento = ? AND censista = ?";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setString(3, dato.getSegmento());
            ps.setString(4, dato.getCensista());
            ps.executeUpdate();
        }
    }

    private void insertarCobCensistaPorVivienda(CobCensistaPorVivienda dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.cob_censista_por_vivienda "
                + "(depto, muni, apoyo_municipal, zona, sector, segmento, censista, area, "
                + "total_viviendas_particulares, total_viviendas_realizadas, personas_ausentes, "
                + "viviendas_con_rechazo, viviendas_transformadas, boletas_efectivas, entrevistas_rechazadas) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dato.getDepto());
            ps.setString(2, dato.getMuni());
            ps.setInt(3, dato.getApoyoMunicipal());
            ps.setInt(4, dato.getZona());
            ps.setInt(5, dato.getSector());
            ps.setString(6, dato.getSegmento());
            ps.setString(7, dato.getCensista());
            ps.setInt(8, dato.getArea());
            ps.setInt(9, dato.getTotalViviendasParticulares());
            ps.setInt(10, dato.getTotalViviendasRealizadas());
            ps.setInt(11, dato.getPersonasAusentes());
            ps.setInt(12, dato.getViviendasConRechazo());
            ps.setInt(13, dato.getViviendasTransformadas());
            ps.setInt(14, dato.getBoletasEfectivas());
            ps.setInt(15, dato.getEntrevistasRechazadas());
            ps.executeUpdate();
        }
    }

    // indicadores_control_nacional siempre tiene una unica fila (foto global del ultimo corte).
    public void refrescarIndicadoresControlNacional(IndicadoresControlNacional dato) throws Exception {

        synchronized (LOCK_INDICADORES_CONTROL_NACIONAL) {
            borrarIndicadoresControlNacional();
            insertarIndicadoresControlNacional(dato);
        }
    }

    private void borrarIndicadoresControlNacional() throws Exception {

        String sql = "DELETE FROM censo_monitoreo.indicadores_control_nacional";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    private void insertarIndicadoresControlNacional(IndicadoresControlNacional dato) throws Exception {

        String sql
                = "INSERT INTO censo_monitoreo.indicadores_control_nacional "
                + "(avance_estructuras_num, avance_estructuras_den, avance_estructuras_pct, "
                + "ocupadas_presentes_num, ocupadas_presentes_den, ocupadas_presentes_pct, "
                + "ocupadas_ausentes_num, ocupadas_ausentes_den, ocupadas_ausentes_pct, "
                + "desocupadas_num, desocupadas_den, desocupadas_pct, "
                + "dist_ocup_presentes, dist_ocup_ausentes, dist_rechazadas, dist_total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getDestinoConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, dato.getAvanceEstructurasNum());
            ps.setInt(2, dato.getAvanceEstructurasDen());
            ps.setDouble(3, dato.getAvanceEstructurasPct());
            ps.setInt(4, dato.getOcupadasPresentesNum());
            ps.setInt(5, dato.getOcupadasPresentesDen());
            ps.setDouble(6, dato.getOcupadasPresentesPct());
            ps.setInt(7, dato.getOcupadasAusentesNum());
            ps.setInt(8, dato.getOcupadasAusentesDen());
            ps.setDouble(9, dato.getOcupadasAusentesPct());
            ps.setInt(10, dato.getDesocupadasNum());
            ps.setInt(11, dato.getDesocupadasDen());
            ps.setDouble(12, dato.getDesocupadasPct());
            ps.setInt(13, dato.getDistOcupPresentes());
            ps.setInt(14, dato.getDistOcupAusentes());
            ps.setInt(15, dato.getDistRechazadas());
            ps.setInt(16, dato.getDistTotal());
            ps.executeUpdate();
        }
    }
}
