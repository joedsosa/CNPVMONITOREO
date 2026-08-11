package hn.gob.ine.listener.dao;

import hn.gob.ine.listener.config.DataSourceFactory;
import hn.gob.ine.listener.model.CalidadEduGrupoEdad;
import hn.gob.ine.listener.model.CalidadEduNivelEducativo;
import hn.gob.ine.listener.model.CobArea;
import hn.gob.ine.listener.model.CobCoberturaDepartamentos;
import hn.gob.ine.listener.model.CobCondicionVivienda;
import hn.gob.ine.listener.model.CobEvolucionCobertura;
import hn.gob.ine.listener.model.CobTotalViviendasDesocupada;
import hn.gob.ine.listener.model.CobTotalViviendasOcupAusentes;
import hn.gob.ine.listener.model.CobTotalViviendasOcupadas;
import hn.gob.ine.listener.model.CobTotalViviendasParticulares;
import hn.gob.ine.listener.model.GeVivCobCensadasSin;
import hn.gob.ine.listener.model.GeVivCobDeptosCompletados;
import hn.gob.ine.listener.model.GeVivCobMunisCompletados;
import hn.gob.ine.listener.model.LlaveCensista;
import hn.gob.ine.listener.model.TotalHogViv;
import hn.gob.ine.listener.model.GeVivCobTipoViv;
import hn.gob.ine.listener.model.CalidadVivVivienda;
import hn.gob.ine.listener.model.CalidadVivHogar;
import hn.gob.ine.listener.model.CalidadHogServicios;
import hn.gob.ine.listener.model.CalidadHogNucleo;
import hn.gob.ine.listener.model.CalidadHogHacinamiento;
import hn.gob.ine.listener.model.CalidadDiscLimitacion;
import hn.gob.ine.listener.model.CalidadEtnIndigenaAfro;
import hn.gob.ine.listener.model.CalidadMigEmigranteGenero;
import hn.gob.ine.listener.model.CalidadMigEmigrantePaisDest;
import hn.gob.ine.listener.model.CalidadMorMortalidad;
import hn.gob.ine.listener.model.CalidadFecFecundidad;
import hn.gob.ine.listener.model.CalidadNacResOtroLugar;
import hn.gob.ine.listener.model.CalidadTicAcceso;
import hn.gob.ine.listener.model.CalidadTraOcuOcupacion;
import hn.gob.ine.listener.model.CalidadCompHogEnvejecimiento;
import hn.gob.ine.listener.model.CalidadCompHogPiramide;
import hn.gob.ine.listener.model.CalidadConyEstado;
import hn.gob.ine.listener.model.CobCensistaProductividad;
import hn.gob.ine.listener.model.CobCensistaPorVivienda;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrigenCnpvDAO {

    public List<LlaveCensista> obtenerCensistasPorDepartamento(String depto) throws Exception {

        List<LlaveCensista> lista = new ArrayList<LlaveCensista>();

        String sql
                = "SELECT DISTINCT "
                + "    LPAD(TRIM(l1.l1_departamento), 2, '0') AS depto, "
                + "    LPAD(TRIM(l1.l1_municipio), 2, '0') AS muni, "
                + "    CAST(COALESCE(NULLIF(TRIM(l1.l1_apoyo_muni), ''), '0') AS UNSIGNED) AS apoyo_muni, "
                + "    CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) AS zona, "
                + "    CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) AS sector, "
                + "    TRIM(l1.l1_segmento) AS segmento, "
                + "    TRIM(l1.l1_cod_encuestador) AS censista "
                + "FROM cnpv_data.`level-1` l1 "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND l1.l1_municipio IS NOT NULL "
                + "  AND TRIM(l1.l1_municipio) <> '' "
                + "  AND l1.l1_segmento IS NOT NULL "
                + "  AND TRIM(l1.l1_segmento) <> '' "
                + "  AND l1.l1_cod_encuestador IS NOT NULL "
                + "  AND TRIM(l1.l1_cod_encuestador) <> '' "
                + "ORDER BY muni, zona, sector, segmento, censista";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, depto);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    LlaveCensista llave = new LlaveCensista(
                            rs.getString("depto"),
                            rs.getString("muni"),
                            rs.getInt("apoyo_muni"),
                            rs.getInt("zona"),
                            rs.getInt("sector"),
                            rs.getString("segmento"),
                            rs.getString("censista")
                    );

                    lista.add(llave);
                }
            }
        }

        return lista;
    }

    public TotalHogViv calcularTotalHogViv(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    COUNT(DISTINCT l1.l1_hogar) AS hogar, "
                + "    COUNT(DISTINCT l1.l1_vivienda) AS vivienda, "
                + "    COUNT(p.`personas_rec-id`) AS cantidad_personas "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.personas_rec p "
                + "    ON p.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TotalHogViv(
                            llave.getDepto(),
                            llave.getMuni(),
                            llave.getApoyoMunicipal(),
                            llave.getZona(),
                            llave.getSector(),
                            llave.getSegmento(),
                            llave.getCensista(),
                            rs.getInt("hogar"),
                            rs.getInt("vivienda"),
                            rs.getInt("cantidad_personas")
                    );
                }
            }
        }

        return new TotalHogViv(
                llave.getDepto(),
                llave.getMuni(),
                llave.getApoyoMunicipal(),
                llave.getZona(),
                llave.getSector(),
                llave.getSegmento(),
                llave.getCensista(),
                0,
                0,
                0
        );
    }

    public CobCondicionVivienda calcularCobCondicionVivienda(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS ocupada_presentes, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS ocupadas_presentes, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) IN (2, 3) THEN 1 ELSE 0 END) AS rechazadas, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 13 THEN 1 ELSE 0 END) AS pendientes "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.visita v "
                + "    ON v.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CobCondicionVivienda(
                            llave.getDepto(),
                            llave.getMuni(),
                            String.valueOf(llave.getSector()),
                            llave.getSegmento(),
                            llave.getCensista(),
                            rs.getInt("ocupada_presentes"),
                            rs.getInt("ocupadas_presentes"),
                            rs.getInt("rechazadas"),
                            rs.getInt("pendientes")
                    );
                }
            }
        }

        return new CobCondicionVivienda(
                llave.getDepto(),
                llave.getMuni(),
                String.valueOf(llave.getSector()),
                llave.getSegmento(),
                llave.getCensista(),
                0,
                0,
                0,
                0
        );
    }

    public CobArea calcularCobArea(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(vr.h_v00_tipo_viv), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS area_urbana, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(vr.h_v00_tipo_viv), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS area_rural "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.vivienda_rec vr "
                + "    ON vr.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CobArea(
                            llave.getDepto(),
                            llave.getMuni(),
                            String.valueOf(llave.getSector()),
                            llave.getSegmento(),
                            llave.getCensista(),
                            rs.getInt("area_urbana"),
                            rs.getInt("area_rural")
                    );
                }
            }
        }

        return new CobArea(
                llave.getDepto(),
                llave.getMuni(),
                String.valueOf(llave.getSector()),
                llave.getSegmento(),
                llave.getCensista(),
                0,
                0
        );
    }

    public List<CobEvolucionCobertura> calcularCobEvolucionCobertura(LlaveCensista llave) throws Exception {

        List<CobEvolucionCobertura> lista = new ArrayList<CobEvolucionCobertura>();

        String sql
                = "SELECT "
                + "    COALESCE(STR_TO_DATE(v.h_fvisita, '%d-%m-%Y'), CURDATE()) AS fecha, "
                + "    COUNT(*) AS avance_dia, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS viv_ocupadas_dia "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.visita v "
                + "    ON v.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ? "
                + "GROUP BY COALESCE(STR_TO_DATE(v.h_fvisita, '%d-%m-%Y'), CURDATE()) "
                + "ORDER BY fecha";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    CobEvolucionCobertura cob = new CobEvolucionCobertura(
                            llave.getDepto(),
                            llave.getMuni(),
                            String.valueOf(llave.getSector()),
                            llave.getSegmento(),
                            llave.getCensista(),
                            rs.getInt("avance_dia"),
                            rs.getInt("viv_ocupadas_dia"),
                            rs.getDate("fecha")
                    );

                    lista.add(cob);
                }
            }
        }

        return lista;
    }

    public GeVivCobTipoViv calcularGeVivCobTipoViv(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(vr.h_v00_tipo_viv), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS viv_particular, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(vr.h_v00_tipo_viv), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS apartamento, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(vr.h_v00_tipo_viv), ''), '0') AS UNSIGNED) = 3 THEN 1 ELSE 0 END) AS cuarteria, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(vr.h_v00_tipo_viv), ''), '0') AS UNSIGNED) NOT IN (1, 2, 3) THEN 1 ELSE 0 END) AS otro_tipo "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.vivienda_rec vr "
                + "    ON vr.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GeVivCobTipoViv(
                            llave.getDepto(),
                            llave.getMuni(),
                            String.valueOf(llave.getSector()),
                            llave.getSegmento(),
                            llave.getCensista(),
                            rs.getInt("viv_particular"),
                            rs.getInt("apartamento"),
                            rs.getInt("cuarteria"),
                            rs.getInt("otro_tipo")
                    );
                }
            }
        }

        return new GeVivCobTipoViv(
                llave.getDepto(),
                llave.getMuni(),
                String.valueOf(llave.getSector()),
                llave.getSegmento(),
                llave.getCensista(),
                0,
                0,
                0,
                0
        );
    }

    public CobTotalViviendasOcupadas calcularCobTotalViviendasOcupadas(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cantidad_ocupadas, "
                + "    COUNT(*) AS cantidad "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.visita v "
                + "    ON v.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getSector());
            ps.setString(4, llave.getSegmento());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CobTotalViviendasOcupadas(
                            llave.getDepto(),
                            llave.getMuni(),
                            String.valueOf(llave.getSector()),
                            llave.getSegmento(),
                            rs.getInt("cantidad_ocupadas"),
                            rs.getInt("cantidad")
                    );
                }
            }
        }

        return new CobTotalViviendasOcupadas(
                llave.getDepto(),
                llave.getMuni(),
                String.valueOf(llave.getSector()),
                llave.getSegmento(),
                0,
                0
        );
    }

    public CobTotalViviendasDesocupada calcularCobTotalViviendasDesocupada(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 5 THEN 1 ELSE 0 END) AS cantidad_desocupada, "
                + "    COUNT(*) AS cantidad "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.visita v "
                + "    ON v.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getSector());
            ps.setString(4, llave.getSegmento());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CobTotalViviendasDesocupada(
                            llave.getDepto(),
                            llave.getMuni(),
                            String.valueOf(llave.getSector()),
                            llave.getSegmento(),
                            rs.getInt("cantidad_desocupada"),
                            rs.getInt("cantidad")
                    );
                }
            }
        }

        return new CobTotalViviendasDesocupada(
                llave.getDepto(),
                llave.getMuni(),
                String.valueOf(llave.getSector()),
                llave.getSegmento(),
                0,
                0
        );
    }

    public CobTotalViviendasOcupAusentes calcularCobTotalViviendasOcupAusentes(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 4 THEN 1 ELSE 0 END) AS cantidad_ocupadas_ausente, "
                + "    COUNT(*) AS cantidad "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.visita v "
                + "    ON v.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getSector());
            ps.setString(4, llave.getSegmento());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CobTotalViviendasOcupAusentes(
                            llave.getDepto(),
                            llave.getMuni(),
                            String.valueOf(llave.getSector()),
                            llave.getSegmento(),
                            rs.getInt("cantidad_ocupadas_ausente"),
                            rs.getInt("cantidad")
                    );
                }
            }
        }

        return new CobTotalViviendasOcupAusentes(
                llave.getDepto(),
                llave.getMuni(),
                String.valueOf(llave.getSector()),
                llave.getSegmento(),
                0,
                0
        );
    }

    public CobTotalViviendasParticulares calcularCobTotalViviendasParticulares(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(vr.h_v00_tipo_viv), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cantidad_particulares, "
                + "    COUNT(*) AS cantidad "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.vivienda_rec vr "
                + "    ON vr.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getSector());
            ps.setString(4, llave.getSegmento());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CobTotalViviendasParticulares(
                            llave.getDepto(),
                            llave.getMuni(),
                            String.valueOf(llave.getSector()),
                            llave.getSegmento(),
                            rs.getInt("cantidad_particulares"),
                            rs.getInt("cantidad")
                    );
                }
            }
        }

        return new CobTotalViviendasParticulares(
                llave.getDepto(),
                llave.getMuni(),
                String.valueOf(llave.getSector()),
                llave.getSegmento(),
                0,
                0
        );
    }

    public GeVivCobCensadasSin calcularGeVivCobCensadasSin(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v06_dispo_agua) = 'No recibe agua por tuberia sino por otros medios' THEN 4 "
                + "         ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v06_dispo_agua), ''), '0') AS UNSIGNED) END) = 4 THEN 1 ELSE 0 END) AS agua, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v08_acc_alum) = 'Electricidad del sistema público' THEN 1 WHEN TRIM(vr.h_v08_acc_alum) = 'Electricidad del sistema privado' THEN 2 WHEN TRIM(vr.h_v08_acc_alum) = 'Electricidad de motor propio' THEN 3 WHEN TRIM(vr.h_v08_acc_alum) = 'Panel solar' THEN 4 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v08_acc_alum), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS energia, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(hr.h_h05_sanit_hog) = 'Inodoro conectado a alcantarilla' THEN 1 ELSE CAST(COALESCE(NULLIF(TRIM(hr.h_h05_sanit_hog), ''), '0') AS UNSIGNED) END) = 3 THEN 1 ELSE 0 END) AS sanitario "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.vivienda_rec vr "
                + "    ON vr.`level-1-id` = l1.`level-1-id` "
                + "LEFT JOIN cnpv_data.hogares_rec hr "
                + "    ON hr.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GeVivCobCensadasSin(
                            llave.getDepto(),
                            llave.getMuni(),
                            String.valueOf(llave.getSector()),
                            llave.getSegmento(),
                            llave.getCensista(),
                            rs.getInt("agua"),
                            rs.getInt("energia"),
                            rs.getInt("sanitario")
                    );
                }
            }
        }

        return new GeVivCobCensadasSin(
                llave.getDepto(),
                llave.getMuni(),
                String.valueOf(llave.getSector()),
                llave.getSegmento(),
                llave.getCensista(),
                0,
                0,
                0
        );
    }

    public CobCoberturaDepartamentos calcularCobCoberturaDepartamentos(String depto) throws Exception {

        String sql
                = "SELECT "
                + "    LPAD(TRIM(l1.l1_departamento), 2, '0') AS depto, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) != 13 THEN 1 ELSE 0 END) AS realizado, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 13 THEN 1 ELSE 0 END) AS por_realizar "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.visita v "
                + "    ON v.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "GROUP BY LPAD(TRIM(l1.l1_departamento), 2, '0')";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, depto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CobCoberturaDepartamentos(
                            rs.getString("depto"),
                            depto,
                            rs.getInt("realizado"),
                            rs.getInt("por_realizar")
                    );
                }
            }
        }

        return new CobCoberturaDepartamentos(depto, depto, 0, 0);
    }

    public GeVivCobDeptosCompletados calcularGeVivCobDeptosCompletados(String depto) throws Exception {

        String sql
                = "SELECT "
                + "    CASE WHEN SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 13 THEN 1 ELSE 0 END) = 0 "
                + "         THEN '1' ELSE '0' END AS completado "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.visita v "
                + "    ON v.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, depto);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GeVivCobDeptosCompletados(
                            depto,
                            rs.getString("completado")
                    );
                }
            }
        }

        return new GeVivCobDeptosCompletados(depto, "0");
    }

    public GeVivCobMunisCompletados calcularGeVivCobMunisCompletados(String depto, String muni) throws Exception {

        String sql
                = "SELECT "
                + "    CASE WHEN SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 13 THEN 1 ELSE 0 END) = 0 "
                + "         THEN '1' ELSE '0' END AS completado "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.visita v "
                + "    ON v.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, depto);
            ps.setString(2, muni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new GeVivCobMunisCompletados(
                            muni,
                            rs.getString("completado")
                    );
                }
            }
        }

        return new GeVivCobMunisCompletados(muni, "0");
    }

    public CalidadVivVivienda calcularCalidadVivVivienda(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa Independiente' THEN 1 WHEN TRIM(vr.h_v01_tipo_viv) = 'Apartamento' THEN 2 WHEN TRIM(vr.h_v01_tipo_viv) = 'Cuarto en meson o cuarteria' THEN 3 WHEN TRIM(vr.h_v01_tipo_viv) = 'Local no construido para vivienda' THEN 4 WHEN TRIM(vr.h_v01_tipo_viv) = 'Rancho (de materiales naturales)' THEN 5 WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa improvisada (material de desecho)' THEN 6 WHEN TRIM(vr.h_v01_tipo_viv) = 'Otro tipo de vivienda particular' THEN 7 WHEN TRIM(vr.h_v01_tipo_viv) IN ('Hotel, pensión, casa de huéspedes', 'Hospital, sanatorio o clínica', 'Orfanato', 'Asilo', 'Cuartel, batallón o posta policial') THEN 8 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v01_tipo_viv), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS tipo_casa_indep, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa Independiente' THEN 1 WHEN TRIM(vr.h_v01_tipo_viv) = 'Apartamento' THEN 2 WHEN TRIM(vr.h_v01_tipo_viv) = 'Cuarto en meson o cuarteria' THEN 3 WHEN TRIM(vr.h_v01_tipo_viv) = 'Local no construido para vivienda' THEN 4 WHEN TRIM(vr.h_v01_tipo_viv) = 'Rancho (de materiales naturales)' THEN 5 WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa improvisada (material de desecho)' THEN 6 WHEN TRIM(vr.h_v01_tipo_viv) = 'Otro tipo de vivienda particular' THEN 7 WHEN TRIM(vr.h_v01_tipo_viv) IN ('Hotel, pensión, casa de huéspedes', 'Hospital, sanatorio o clínica', 'Orfanato', 'Asilo', 'Cuartel, batallón o posta policial') THEN 8 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v01_tipo_viv), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS tipo_apartamento, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa Independiente' THEN 1 WHEN TRIM(vr.h_v01_tipo_viv) = 'Apartamento' THEN 2 WHEN TRIM(vr.h_v01_tipo_viv) = 'Cuarto en meson o cuarteria' THEN 3 WHEN TRIM(vr.h_v01_tipo_viv) = 'Local no construido para vivienda' THEN 4 WHEN TRIM(vr.h_v01_tipo_viv) = 'Rancho (de materiales naturales)' THEN 5 WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa improvisada (material de desecho)' THEN 6 WHEN TRIM(vr.h_v01_tipo_viv) = 'Otro tipo de vivienda particular' THEN 7 WHEN TRIM(vr.h_v01_tipo_viv) IN ('Hotel, pensión, casa de huéspedes', 'Hospital, sanatorio o clínica', 'Orfanato', 'Asilo', 'Cuartel, batallón o posta policial') THEN 8 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v01_tipo_viv), ''), '0') AS UNSIGNED) END) = 3 THEN 1 ELSE 0 END) AS tipo_anexo, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa Independiente' THEN 1 WHEN TRIM(vr.h_v01_tipo_viv) = 'Apartamento' THEN 2 WHEN TRIM(vr.h_v01_tipo_viv) = 'Cuarto en meson o cuarteria' THEN 3 WHEN TRIM(vr.h_v01_tipo_viv) = 'Local no construido para vivienda' THEN 4 WHEN TRIM(vr.h_v01_tipo_viv) = 'Rancho (de materiales naturales)' THEN 5 WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa improvisada (material de desecho)' THEN 6 WHEN TRIM(vr.h_v01_tipo_viv) = 'Otro tipo de vivienda particular' THEN 7 WHEN TRIM(vr.h_v01_tipo_viv) IN ('Hotel, pensión, casa de huéspedes', 'Hospital, sanatorio o clínica', 'Orfanato', 'Asilo', 'Cuartel, batallón o posta policial') THEN 8 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v01_tipo_viv), ''), '0') AS UNSIGNED) END) = 4 THEN 1 ELSE 0 END) AS tipo_cuarto_meson, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa Independiente' THEN 1 WHEN TRIM(vr.h_v01_tipo_viv) = 'Apartamento' THEN 2 WHEN TRIM(vr.h_v01_tipo_viv) = 'Cuarto en meson o cuarteria' THEN 3 WHEN TRIM(vr.h_v01_tipo_viv) = 'Local no construido para vivienda' THEN 4 WHEN TRIM(vr.h_v01_tipo_viv) = 'Rancho (de materiales naturales)' THEN 5 WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa improvisada (material de desecho)' THEN 6 WHEN TRIM(vr.h_v01_tipo_viv) = 'Otro tipo de vivienda particular' THEN 7 WHEN TRIM(vr.h_v01_tipo_viv) IN ('Hotel, pensión, casa de huéspedes', 'Hospital, sanatorio o clínica', 'Orfanato', 'Asilo', 'Cuartel, batallón o posta policial') THEN 8 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v01_tipo_viv), ''), '0') AS UNSIGNED) END) = 5 THEN 1 ELSE 0 END) AS tipo_no_construido, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa Independiente' THEN 1 WHEN TRIM(vr.h_v01_tipo_viv) = 'Apartamento' THEN 2 WHEN TRIM(vr.h_v01_tipo_viv) = 'Cuarto en meson o cuarteria' THEN 3 WHEN TRIM(vr.h_v01_tipo_viv) = 'Local no construido para vivienda' THEN 4 WHEN TRIM(vr.h_v01_tipo_viv) = 'Rancho (de materiales naturales)' THEN 5 WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa improvisada (material de desecho)' THEN 6 WHEN TRIM(vr.h_v01_tipo_viv) = 'Otro tipo de vivienda particular' THEN 7 WHEN TRIM(vr.h_v01_tipo_viv) IN ('Hotel, pensión, casa de huéspedes', 'Hospital, sanatorio o clínica', 'Orfanato', 'Asilo', 'Cuartel, batallón o posta policial') THEN 8 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v01_tipo_viv), ''), '0') AS UNSIGNED) END) = 6 THEN 1 ELSE 0 END) AS tipo_rancho, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa Independiente' THEN 1 WHEN TRIM(vr.h_v01_tipo_viv) = 'Apartamento' THEN 2 WHEN TRIM(vr.h_v01_tipo_viv) = 'Cuarto en meson o cuarteria' THEN 3 WHEN TRIM(vr.h_v01_tipo_viv) = 'Local no construido para vivienda' THEN 4 WHEN TRIM(vr.h_v01_tipo_viv) = 'Rancho (de materiales naturales)' THEN 5 WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa improvisada (material de desecho)' THEN 6 WHEN TRIM(vr.h_v01_tipo_viv) = 'Otro tipo de vivienda particular' THEN 7 WHEN TRIM(vr.h_v01_tipo_viv) IN ('Hotel, pensión, casa de huéspedes', 'Hospital, sanatorio o clínica', 'Orfanato', 'Asilo', 'Cuartel, batallón o posta policial') THEN 8 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v01_tipo_viv), ''), '0') AS UNSIGNED) END) = 7 THEN 1 ELSE 0 END) AS tipo_improvisado, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa Independiente' THEN 1 WHEN TRIM(vr.h_v01_tipo_viv) = 'Apartamento' THEN 2 WHEN TRIM(vr.h_v01_tipo_viv) = 'Cuarto en meson o cuarteria' THEN 3 WHEN TRIM(vr.h_v01_tipo_viv) = 'Local no construido para vivienda' THEN 4 WHEN TRIM(vr.h_v01_tipo_viv) = 'Rancho (de materiales naturales)' THEN 5 WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa improvisada (material de desecho)' THEN 6 WHEN TRIM(vr.h_v01_tipo_viv) = 'Otro tipo de vivienda particular' THEN 7 WHEN TRIM(vr.h_v01_tipo_viv) IN ('Hotel, pensión, casa de huéspedes', 'Hospital, sanatorio o clínica', 'Orfanato', 'Asilo', 'Cuartel, batallón o posta policial') THEN 8 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v01_tipo_viv), ''), '0') AS UNSIGNED) END) = 8 THEN 1 ELSE 0 END) AS tipo_otro, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v05_piso_viv) = 'Tierra' THEN 5 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v05_piso_viv), ''), '0') AS UNSIGNED) END) = 5 THEN 1 ELSE 0 END) AS piso_tierra, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v03_techo_viv) = 'Teja de barro' THEN 1 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v03_techo_viv), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS techo_teja, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v07_prov_agua) = 'Del sistema público' THEN 1 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v07_prov_agua), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS recibe_agua, "
                + "    COUNT(*) AS cantidad "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.vivienda_rec vr "
                + "    ON vr.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadVivVivienda(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("tipo_casa_indep"), rs.getInt("tipo_apartamento"),
                            rs.getInt("tipo_anexo"), rs.getInt("tipo_cuarto_meson"),
                            rs.getInt("tipo_no_construido"), rs.getInt("tipo_rancho"),
                            rs.getInt("tipo_improvisado"), rs.getInt("tipo_otro"),
                            rs.getInt("piso_tierra"), rs.getInt("techo_teja"),
                            rs.getInt("recibe_agua"), rs.getInt("cantidad")
                    );
                }
            }
        }

        return new CalidadVivVivienda(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadVivHogar calcularCalidadVivHogar(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    COUNT(*) AS cantidad, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v08_acc_alum) = 'Electricidad del sistema público' THEN 1 WHEN TRIM(vr.h_v08_acc_alum) = 'Electricidad del sistema privado' THEN 2 WHEN TRIM(vr.h_v08_acc_alum) = 'Electricidad de motor propio' THEN 3 WHEN TRIM(vr.h_v08_acc_alum) = 'Panel solar' THEN 4 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v08_acc_alum), ''), '0') AS UNSIGNED) END) IN (1,2,3,4) THEN 1 ELSE 0 END) AS electricidad, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(hr.h_h07j_internet), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS internet, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(hr.h_h05_sanit_hog) = 'Inodoro conectado a alcantarilla' THEN 1 ELSE CAST(COALESCE(NULLIF(TRIM(hr.h_h05_sanit_hog), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS tipo_sanitario_alcant, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(vr.h_v09_elim_basura) = 'Recolección domiciliaria (tren de aseo)' THEN 1 ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v09_elim_basura), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS tren_aseo "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.vivienda_rec vr "
                + "    ON vr.`level-1-id` = l1.`level-1-id` "
                + "LEFT JOIN cnpv_data.hogares_rec hr "
                + "    ON hr.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadVivHogar(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("cantidad"), rs.getInt("electricidad"),
                            rs.getInt("internet"), rs.getInt("tipo_sanitario_alcant"),
                            rs.getInt("tren_aseo")
                    );
                }
            }
        }

        return new CalidadVivHogar(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0
        );
    }

    public CalidadHogServicios calcularCalidadHogServicios(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    COUNT(*) AS cantidad, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(hr.h_h07a_refri), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS refrigeradora, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(hr.h_h07f_tv), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS televisor, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(hr.h_h07g_pc), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS computadora, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(hr.h_h07i_tel_cel), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS celular, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(hr.h_h07j_internet), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS internet "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.hogares_rec hr "
                + "    ON hr.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadHogServicios(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("cantidad"), rs.getInt("refrigeradora"),
                            rs.getInt("televisor"), rs.getInt("computadora"),
                            rs.getInt("celular"), rs.getInt("internet")
                    );
                }
            }
        }

        return new CalidadHogServicios(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0
        );
    }

    public CalidadHogNucleo calcularCalidadHogNucleo(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch02_parentesco), ''), '0') AS UNSIGNED) = 1 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS jefe_mujer, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch02_parentesco), ''), '0') AS UNSIGNED) = 1 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS jefe_hombre, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(hr.h_h09a_adultos), ''), '0') AS UNSIGNED) = 2 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(hr.h_ch00_num_per), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS biparental_sin_hijos, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(hr.h_h09a_adultos), ''), '0') AS UNSIGNED) = 2 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(hr.h_ch00_num_per), ''), '0') AS UNSIGNED) > 2 THEN 1 ELSE 0 END) AS biparental_con_hijos, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(hr.h_ch00_num_per), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS unipersonal, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(hr.h_h09a_adultos), ''), '0') AS UNSIGNED) = 1 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(hr.h_ch00_num_per), ''), '0') AS UNSIGNED) > 1 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_ch02_parentesco), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS monoparental_jefe_mujer "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.hogares_rec hr "
                + "    ON hr.`level-1-id` = l1.`level-1-id` "
                + "LEFT JOIN cnpv_data.personas_rec p "
                + "    ON p.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadHogNucleo(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("jefe_mujer"), rs.getInt("jefe_hombre"),
                            rs.getInt("biparental_sin_hijos"), rs.getInt("biparental_con_hijos"),
                            rs.getInt("unipersonal"), rs.getInt("monoparental_jefe_mujer")
                    );
                }
            }
        }

        return new CalidadHogNucleo(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0
        );
    }

    public CalidadHogHacinamiento calcularCalidadHogHacinamiento(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN (CAST(COALESCE(NULLIF(TRIM(hr.h_ch00_num_per), ''), '0') AS UNSIGNED) / "
                + "        NULLIF(CAST(COALESCE(NULLIF(TRIM(hr.h_h01_pieza_dormir), ''), '0') AS UNSIGNED), 0)) > 3 THEN 1 ELSE 0 END) AS mayor_3_hac, "
                + "    SUM(CAST(COALESCE(NULLIF(TRIM(hr.h_ch00_num_per), ''), '0') AS UNSIGNED)) AS num_personas "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.hogares_rec hr "
                + "    ON hr.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadHogHacinamiento(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("mayor_3_hac"), rs.getInt("num_personas")
                    );
                }
            }
        }

        return new CalidadHogHacinamiento(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0
        );
    }

    public CalidadEduGrupoEdad calcularCalidadEduGrupoEdad(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 5 AND 6 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p18_asisten_edu), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edu_edad_5_6, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 5 AND 12 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p18_asisten_edu), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edu_edad_5_12, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 5 AND 17 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p18_asisten_edu), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edu_edad_5_17, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 6 AND 12 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p18_asisten_edu), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edu_edad_6_12, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 7 AND 12 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p18_asisten_edu), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edu_edad_7_12, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 13 AND 17 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p18_asisten_edu), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edu_edad_13_17, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 6 AND 12 THEN 1 ELSE 0 END) AS poblacion_6_12, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 7 AND 12 THEN 1 ELSE 0 END) AS poblacion_7_12, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 5 AND 6 THEN 1 ELSE 0 END) AS poblacion_5_6, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 13 AND 17 THEN 1 ELSE 0 END) AS poblacion_13_17, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 5 AND 17 THEN 1 ELSE 0 END) AS poblacion_5_17, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 3 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p18_asisten_edu), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edu_edad_3_mas, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 3 THEN 1 ELSE 0 END) AS poblacion_3_mas "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.personas_rec p "
                + "    ON p.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadEduGrupoEdad(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("edu_edad_5_6"), rs.getInt("edu_edad_5_12"),
                            rs.getInt("edu_edad_5_17"), rs.getInt("edu_edad_6_12"),
                            rs.getInt("edu_edad_7_12"), rs.getInt("edu_edad_13_17"),
                            rs.getInt("poblacion_6_12"), rs.getInt("poblacion_7_12"),
                            rs.getInt("poblacion_5_6"), rs.getInt("poblacion_13_17"),
                            rs.getInt("poblacion_5_17"),
                            rs.getInt("edu_edad_3_mas"), rs.getInt("poblacion_3_mas")
                    );
                }
            }
        }

        return new CalidadEduGrupoEdad(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadEduNivelEducativo calcularCalidadEduNivelEducativo(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p19a_nivel_educ), ''), '0') AS UNSIGNED) = 0 THEN 1 ELSE 0 END) AS cant_no_educ, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p19a_nivel_educ), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_alfabetizacion, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p19a_nivel_educ), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_prebasica, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p19a_nivel_educ), ''), '0') AS UNSIGNED) = 3 THEN 1 ELSE 0 END) AS cant_basica, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p19a_nivel_educ), ''), '0') AS UNSIGNED) = 4 THEN 1 ELSE 0 END) AS cant_media, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p19a_nivel_educ), ''), '0') AS UNSIGNED) = 5 THEN 1 ELSE 0 END) AS cant_tec_superior, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p19a_nivel_educ), ''), '0') AS UNSIGNED) = 6 THEN 1 ELSE 0 END) AS cant_tec_no_superior, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p19a_nivel_educ), ''), '0') AS UNSIGNED) = 7 THEN 1 ELSE 0 END) AS cant_universidad, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p19a_nivel_educ), ''), '0') AS UNSIGNED) = 8 THEN 1 ELSE 0 END) AS cant_especialidad, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p19a_nivel_educ), ''), '0') AS UNSIGNED) = 9 THEN 1 ELSE 0 END) AS cant_maestria, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p19a_nivel_educ), ''), '0') AS UNSIGNED) = 10 THEN 1 ELSE 0 END) AS cant_doctorado, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 15 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p22_leer_escri), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_analfabeta, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 15 THEN 1 ELSE 0 END) AS poblacion_15_mas, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 15 THEN "
                + "        CAST(COALESCE(NULLIF(TRIM(p.h_p19b_grado_educ), ''), '0') AS UNSIGNED) ELSE 0 END) AS total_anios_estudio, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 15 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p19a_nivel_educ), ''), '0') AS UNSIGNED) > 0 THEN 1 ELSE 0 END) AS poblacion_para_promedio, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 5 THEN 1 ELSE 0 END) AS poblacion_nivel_educativo "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.personas_rec p "
                + "    ON p.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadEduNivelEducativo(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("cant_no_educ"), rs.getInt("cant_alfabetizacion"),
                            rs.getInt("cant_prebasica"), rs.getInt("cant_basica"),
                            rs.getInt("cant_media"), rs.getInt("cant_tec_superior"),
                            rs.getInt("cant_tec_no_superior"), rs.getInt("cant_universidad"),
                            rs.getInt("cant_especialidad"), rs.getInt("cant_maestria"),
                            rs.getInt("cant_doctorado"), rs.getInt("cant_analfabeta"),
                            rs.getInt("poblacion_15_mas"), rs.getInt("total_anios_estudio"),
                            rs.getInt("poblacion_para_promedio"), rs.getInt("poblacion_nivel_educativo")
                    );
                }
            }
        }

        return new CalidadEduNivelEducativo(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadDiscLimitacion calcularCalidadDiscLimitacion(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p07a_caminar_c), ''), '0') AS UNSIGNED) IN (3, 4) "
                + "        OR CAST(COALESCE(NULLIF(TRIM(p.h_p07b_comuni_c), ''), '0') AS UNSIGNED) IN (3, 4) "
                + "        OR CAST(COALESCE(NULLIF(TRIM(p.h_p07c_ver_c), ''), '0') AS UNSIGNED) IN (3, 4) "
                + "        OR CAST(COALESCE(NULLIF(TRIM(p.h_p07d_oir_c), ''), '0') AS UNSIGNED) IN (3, 4) "
                + "        OR CAST(COALESCE(NULLIF(TRIM(p.h_p07e_valerse_c), ''), '0') AS UNSIGNED) IN (3, 4) "
                + "        OR CAST(COALESCE(NULLIF(TRIM(p.h_p07f_recordar_c), ''), '0') AS UNSIGNED) IN (3, 4) "
                + "        OR CAST(COALESCE(NULLIF(TRIM(p.h_p07g_brazos_c), ''), '0') AS UNSIGNED) IN (3, 4) "
                + "        THEN 1 ELSE 0 END) AS cant_limitacion, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p07a_caminar_c), ''), '0') AS UNSIGNED) IN (3, 4) THEN 1 ELSE 0 END) AS cant_lim_caminar, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p07b_comuni_c), ''), '0') AS UNSIGNED) IN (3, 4) THEN 1 ELSE 0 END) AS cant_lim_habla, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p07c_ver_c), ''), '0') AS UNSIGNED) IN (3, 4) THEN 1 ELSE 0 END) AS cant_lim_vision, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p07d_oir_c), ''), '0') AS UNSIGNED) IN (3, 4) THEN 1 ELSE 0 END) AS cant_lim_oir, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p07f_recordar_c), ''), '0') AS UNSIGNED) IN (3, 4) THEN 1 ELSE 0 END) AS cant_lim_aprender, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p07g_brazos_c), ''), '0') AS UNSIGNED) IN (3, 4) THEN 1 ELSE 0 END) AS cant_lim_brazo, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p07e_valerse_c), ''), '0') AS UNSIGNED) IN (3, 4) THEN 1 ELSE 0 END) AS cant_lim_valerse, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p07a_caminar_c), ''), '0') AS UNSIGNED) NOT IN (3, 4) "
                + "        AND CAST(COALESCE(NULLIF(TRIM(p.h_p07b_comuni_c), ''), '0') AS UNSIGNED) NOT IN (3, 4) "
                + "        AND CAST(COALESCE(NULLIF(TRIM(p.h_p07c_ver_c), ''), '0') AS UNSIGNED) NOT IN (3, 4) "
                + "        AND CAST(COALESCE(NULLIF(TRIM(p.h_p07d_oir_c), ''), '0') AS UNSIGNED) NOT IN (3, 4) "
                + "        AND CAST(COALESCE(NULLIF(TRIM(p.h_p07e_valerse_c), ''), '0') AS UNSIGNED) NOT IN (3, 4) "
                + "        AND CAST(COALESCE(NULLIF(TRIM(p.h_p07f_recordar_c), ''), '0') AS UNSIGNED) NOT IN (3, 4) "
                + "        AND CAST(COALESCE(NULLIF(TRIM(p.h_p07g_brazos_c), ''), '0') AS UNSIGNED) NOT IN (3, 4) "
                + "        THEN 1 ELSE 0 END) AS cant_sin_limitacion "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.personas_rec p "
                + "    ON p.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadDiscLimitacion(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("cant_limitacion"), rs.getInt("cant_lim_caminar"),
                            rs.getInt("cant_lim_habla"), rs.getInt("cant_lim_vision"),
                            rs.getInt("cant_lim_oir"), rs.getInt("cant_lim_aprender"),
                            rs.getInt("cant_lim_brazo"), rs.getInt("cant_lim_valerse"),
                            rs.getInt("cant_sin_limitacion")
                    );
                }
            }
        }

        return new CalidadDiscLimitacion(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadEtnIndigenaAfro calcularCalidadEtnIndigenaAfro(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_mujer, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_hombre, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p01_pueblo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_indigena, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p03_pueblo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_afro, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p01_pueblo), ''), '0') AS UNSIGNED) = 1 "
                + "        AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_indigena_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p01_pueblo), ''), '0') AS UNSIGNED) = 1 "
                + "        AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_indigena_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p03_pueblo), ''), '0') AS UNSIGNED) = 1 "
                + "        AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_afro_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p03_pueblo), ''), '0') AS UNSIGNED) = 1 "
                + "        AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_afro_m "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.personas_rec p "
                + "    ON p.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadEtnIndigenaAfro(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("cant_mujer"), rs.getInt("cant_hombre"),
                            rs.getInt("cant_indigena"), rs.getInt("cant_afro"),
                            rs.getInt("cant_indigena_h"), rs.getInt("cant_indigena_m"),
                            rs.getInt("cant_afro_h"), rs.getInt("cant_afro_m")
                    );
                }
            }
        }

        return new CalidadEtnIndigenaAfro(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadMigEmigranteGenero calcularCalidadMigEmigranteGenero(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS cant_mujer_emi, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS cant_hombre_emi, "
                + "    COUNT(DISTINCT CASE WHEN e.`emigracion_rec-id` IS NOT NULL THEN l1.`level-1-id` END) AS hogares_con_emigrante, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 0 AND 4  AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_0_4_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 0 AND 4  AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_0_4_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 5 AND 9  AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_5_9_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 5 AND 9  AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_5_9_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 10 AND 14 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_10_14_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 10 AND 14 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_10_14_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 15 AND 19 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_15_19_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 15 AND 19 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_15_19_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 20 AND 24 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_20_24_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 20 AND 24 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_20_24_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 25 AND 29 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_25_29_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 25 AND 29 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_25_29_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 30 AND 34 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_30_34_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 30 AND 34 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_30_34_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 35 AND 39 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_35_39_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 35 AND 39 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_35_39_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 40 AND 44 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_40_44_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 40 AND 44 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_40_44_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 45 AND 49 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_45_49_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 45 AND 49 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_45_49_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 50 AND 54 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_50_54_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 50 AND 54 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_50_54_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 55 AND 59 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_55_59_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 55 AND 59 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_55_59_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 60 AND 64 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_60_64_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 60 AND 64 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_60_64_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 65 AND 69 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_65_69_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 65 AND 69 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_65_69_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 70 AND 74 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_70_74_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 70 AND 74 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_70_74_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 75 AND 79 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_75_79_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) BETWEEN 75 AND 79 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_75_79_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) >= 80 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS edad_80_mas_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e03_edad_emi), ''), '0') AS UNSIGNED) >= 80 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS edad_80_mas_m "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.emigracion_rec e "
                + "    ON e.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadMigEmigranteGenero(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("cant_mujer_emi"), rs.getInt("cant_hombre_emi"),
                            rs.getInt("hogares_con_emigrante"),
                            rs.getInt("edad_0_4_h"), rs.getInt("edad_0_4_m"),
                            rs.getInt("edad_5_9_h"), rs.getInt("edad_5_9_m"),
                            rs.getInt("edad_10_14_h"), rs.getInt("edad_10_14_m"),
                            rs.getInt("edad_15_19_h"), rs.getInt("edad_15_19_m"),
                            rs.getInt("edad_20_24_h"), rs.getInt("edad_20_24_m"),
                            rs.getInt("edad_25_29_h"), rs.getInt("edad_25_29_m"),
                            rs.getInt("edad_30_34_h"), rs.getInt("edad_30_34_m"),
                            rs.getInt("edad_35_39_h"), rs.getInt("edad_35_39_m"),
                            rs.getInt("edad_40_44_h"), rs.getInt("edad_40_44_m"),
                            rs.getInt("edad_45_49_h"), rs.getInt("edad_45_49_m"),
                            rs.getInt("edad_50_54_h"), rs.getInt("edad_50_54_m"),
                            rs.getInt("edad_55_59_h"), rs.getInt("edad_55_59_m"),
                            rs.getInt("edad_60_64_h"), rs.getInt("edad_60_64_m"),
                            rs.getInt("edad_65_69_h"), rs.getInt("edad_65_69_m"),
                            rs.getInt("edad_70_74_h"), rs.getInt("edad_70_74_m"),
                            rs.getInt("edad_75_79_h"), rs.getInt("edad_75_79_m"),
                            rs.getInt("edad_80_mas_h"), rs.getInt("edad_80_mas_m")
                    );
                }
            }
        }

        return new CalidadMigEmigranteGenero(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadMigEmigrantePaisDest calcularCalidadMigEmigrantePaisDest(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS cant_mujer, "
                + "    SUM(CASE WHEN (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS cant_hombre, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS pais_1, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS pais_2, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 3 THEN 1 ELSE 0 END) AS pais_3, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 4 THEN 1 ELSE 0 END) AS pais_4, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) NOT IN (1,2,3,4,5) THEN 1 ELSE 0 END) AS pais_otro, "
                + "    COUNT(*) AS cantidad, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 1 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS pais_eeuu_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 1 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS pais_eeuu_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 2 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS pais_espana_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 2 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS pais_espana_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 3 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS pais_mexico_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 3 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS pais_mexico_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 4 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS pais_canada_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 4 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS pais_canada_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 5 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS pais_italia_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) = 5 AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS pais_italia_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) NOT IN (1,2,3,4,5) AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 1 THEN 1 ELSE 0 END) AS pais_otro_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(e.h_e05_resi_emi), ''), '0') AS UNSIGNED) NOT IN (1,2,3,4,5) AND (CASE WHEN TRIM(e.h_e02_sexo_emi) = 'Hombre' THEN 1 WHEN TRIM(e.h_e02_sexo_emi) = 'Mujer' THEN 2 ELSE CAST(COALESCE(NULLIF(TRIM(e.h_e02_sexo_emi), ''), '0') AS UNSIGNED) END) = 2 THEN 1 ELSE 0 END) AS pais_otro_m "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.emigracion_rec e "
                + "    ON e.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadMigEmigrantePaisDest(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("cant_mujer"), rs.getInt("cant_hombre"),
                            rs.getInt("pais_1"), rs.getInt("pais_2"), rs.getInt("pais_3"),
                            rs.getInt("pais_4"), rs.getInt("pais_otro"), rs.getInt("cantidad"),
                            rs.getInt("pais_eeuu_h"), rs.getInt("pais_eeuu_m"),
                            rs.getInt("pais_espana_h"), rs.getInt("pais_espana_m"),
                            rs.getInt("pais_mexico_h"), rs.getInt("pais_mexico_m"),
                            rs.getInt("pais_canada_h"), rs.getInt("pais_canada_m"),
                            rs.getInt("pais_italia_h"), rs.getInt("pais_italia_m"),
                            rs.getInt("pais_otro_h"), rs.getInt("pais_otro_m")
                    );
                }
            }
        }

        return new CalidadMigEmigrantePaisDest(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadMorMortalidad calcularCalidadMorMortalidad(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_hombre, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_mujer, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) = 0  AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_0_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) = 0  AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_0_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 1 AND 4   AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_1_4_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 1 AND 4   AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_1_4_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 5 AND 14  AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_5_14_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 5 AND 14  AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_5_14_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 15 AND 24 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_15_24_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 15 AND 24 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_15_24_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 25 AND 34 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_25_34_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 25 AND 34 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_25_34_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 35 AND 44 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_35_44_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 35 AND 44 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_35_44_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 45 AND 54 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_45_54_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 45 AND 54 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_45_54_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 55 AND 64 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_55_64_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 55 AND 64 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_55_64_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 65 AND 74 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_65_74_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) BETWEEN 65 AND 74 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_65_74_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) >= 75 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_75_mas_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(mo.h_m04_mort_edad), ''), '0') AS UNSIGNED) >= 75 AND CAST(COALESCE(NULLIF(TRIM(mo.h_m03_mort_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_75_mas_m, "
                + "    COUNT(*) AS cantidad, "
                + "    COUNT(DISTINCT CASE WHEN mo.`mortalidad_rec-id` IS NOT NULL THEN l1.`level-1-id` END) AS hogar_con_un_fallecido "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.mortalidad_rec mo "
                + "    ON mo.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadMorMortalidad(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("cant_hombre"), rs.getInt("cant_mujer"),
                            rs.getInt("edad_0_h"), rs.getInt("edad_0_m"),
                            rs.getInt("edad_1_4_h"), rs.getInt("edad_1_4_m"),
                            rs.getInt("edad_5_14_h"), rs.getInt("edad_5_14_m"),
                            rs.getInt("edad_15_24_h"), rs.getInt("edad_15_24_m"),
                            rs.getInt("edad_25_34_h"), rs.getInt("edad_25_34_m"),
                            rs.getInt("edad_35_44_h"), rs.getInt("edad_35_44_m"),
                            rs.getInt("edad_45_54_h"), rs.getInt("edad_45_54_m"),
                            rs.getInt("edad_55_64_h"), rs.getInt("edad_55_64_m"),
                            rs.getInt("edad_65_74_h"), rs.getInt("edad_65_74_m"),
                            rs.getInt("edad_75_mas_h"), rs.getInt("edad_75_mas_m"),
                            rs.getInt("cantidad"), rs.getInt("hogar_con_un_fallecido")
                    );
                }
            }
        }

        return new CalidadMorMortalidad(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadFecFecundidad calcularCalidadFecFecundidad(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 15 AND 49 THEN 1 ELSE 0 END) AS cant_mujeres_15_49_anios, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 15 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) > 0 THEN 1 ELSE 0 END) AS cant_mujer_15_con_hijos, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 15 AND 49 "
                + "             THEN CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) ELSE 0 END) AS cant_hijos_nacidos_15_49, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 15 "
                + "             THEN CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) ELSE 0 END) AS cant_hijos_nacidos, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 15 AND 19 THEN 1 ELSE 0 END) AS edad_15_19, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 20 AND 24 THEN 1 ELSE 0 END) AS edad_20_24, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 25 AND 29 THEN 1 ELSE 0 END) AS edad_25_29, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 30 AND 34 THEN 1 ELSE 0 END) AS edad_30_34, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 35 AND 39 THEN 1 ELSE 0 END) AS edad_35_39, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 40 AND 44 THEN 1 ELSE 0 END) AS edad_40_44, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 45 AND 49 THEN 1 ELSE 0 END) AS edad_45_49, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 15 AND 19 AND CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) > 0 THEN 1 ELSE 0 END) AS primer_hijo_15_19, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 20 AND 24 AND CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) > 0 THEN 1 ELSE 0 END) AS primer_hijo_20_24, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 25 AND 29 AND CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) > 0 THEN 1 ELSE 0 END) AS primer_hijo_25_29, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 30 AND 34 AND CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) > 0 THEN 1 ELSE 0 END) AS primer_hijo_30_34, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 35 AND 39 AND CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) > 0 THEN 1 ELSE 0 END) AS primer_hijo_35_39, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 40 AND 44 AND CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) > 0 THEN 1 ELSE 0 END) AS primer_hijo_40_44, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 45 AND 49 AND CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) > 0 THEN 1 ELSE 0 END) AS primer_hijo_45_49, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS total_mujeres, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 15 THEN 1 ELSE 0 END) AS total_mujeres_15_mas, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 15 AND 19 THEN CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) ELSE 0 END) AS hijos_15_19, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 20 AND 24 THEN CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) ELSE 0 END) AS hijos_20_24, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 25 AND 29 THEN CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) ELSE 0 END) AS hijos_25_29, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 30 AND 34 THEN CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) ELSE 0 END) AS hijos_30_34, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 35 AND 39 THEN CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) ELSE 0 END) AS hijos_35_39, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 40 AND 44 THEN CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) ELSE 0 END) AS hijos_40_44, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 45 AND 49 THEN CAST(COALESCE(NULLIF(TRIM(p.h_p39_total_hijos), ''), '0') AS UNSIGNED) ELSE 0 END) AS hijos_45_49 "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.personas_rec p "
                + "    ON p.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadFecFecundidad(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(),
                            rs.getInt("cant_mujeres_15_49_anios"), rs.getInt("cant_mujer_15_con_hijos"),
                            rs.getInt("cant_hijos_nacidos_15_49"), rs.getInt("cant_hijos_nacidos"),
                            rs.getInt("edad_15_19"), rs.getInt("edad_20_24"),
                            rs.getInt("edad_25_29"), rs.getInt("edad_30_34"),
                            rs.getInt("edad_35_39"), rs.getInt("edad_40_44"),
                            rs.getInt("edad_45_49"),
                            rs.getInt("primer_hijo_15_19"), rs.getInt("primer_hijo_20_24"),
                            rs.getInt("primer_hijo_25_29"), rs.getInt("primer_hijo_30_34"),
                            rs.getInt("primer_hijo_35_39"), rs.getInt("primer_hijo_40_44"),
                            rs.getInt("primer_hijo_45_49"),
                            rs.getInt("total_mujeres"), rs.getInt("total_mujeres_15_mas"),
                            rs.getInt("hijos_15_19"), rs.getInt("hijos_20_24"),
                            rs.getInt("hijos_25_29"), rs.getInt("hijos_30_34"),
                            rs.getInt("hijos_35_39"), rs.getInt("hijos_40_44"),
                            rs.getInt("hijos_45_49")
                    );
                }
            }
        }

        return new CalidadFecFecundidad(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(),
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadTraOcuOcupacion calcularCalidadTraOcuOcupacion(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_hombre, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_mujer, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 "
                + "        AND (CAST(COALESCE(NULLIF(TRIM(p.h_p24_ident_empleado), ''), '0') AS UNSIGNED) IN (1,2,3) "
                + "         OR CAST(COALESCE(NULLIF(TRIM(p.h_p25_productos_agro), ''), '0') AS UNSIGNED) IN (1,2) "
                + "         OR CAST(COALESCE(NULLIF(TRIM(p.h_p26_acti_reali), ''), '0') AS UNSIGNED) IN (1,2,3)) THEN 1 ELSE 0 END) AS cant_p_ocupada_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 "
                + "        AND (CAST(COALESCE(NULLIF(TRIM(p.h_p24_ident_empleado), ''), '0') AS UNSIGNED) IN (1,2,3) "
                + "         OR CAST(COALESCE(NULLIF(TRIM(p.h_p25_productos_agro), ''), '0') AS UNSIGNED) IN (1,2) "
                + "         OR CAST(COALESCE(NULLIF(TRIM(p.h_p26_acti_reali), ''), '0') AS UNSIGNED) IN (1,2,3)) THEN 1 ELSE 0 END) AS cant_p_ocupada_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 "
                + "        AND (CAST(COALESCE(NULLIF(TRIM(p.h_p27_busc_trab), ''), '0') AS UNSIGNED) = 1 "
                + "         OR CAST(COALESCE(NULLIF(TRIM(p.h_p28_disponibilidad), ''), '0') AS UNSIGNED) = 1) THEN 1 ELSE 0 END) AS cant_p_desocupada_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 "
                + "        AND (CAST(COALESCE(NULLIF(TRIM(p.h_p27_busc_trab), ''), '0') AS UNSIGNED) = 1 "
                + "         OR CAST(COALESCE(NULLIF(TRIM(p.h_p28_disponibilidad), ''), '0') AS UNSIGNED) = 1) THEN 1 ELSE 0 END) AS cant_p_desocupada_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 15 AND CAST(COALESCE(NULLIF(TRIM(p.h_p29_act_princ), ''), '0') AS UNSIGNED) = 5 THEN 1 ELSE 0 END) AS cant_retirada_edad_15_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 15 AND CAST(COALESCE(NULLIF(TRIM(p.h_p29_act_princ), ''), '0') AS UNSIGNED) = 5 THEN 1 ELSE 0 END) AS cant_retirada_edad_15_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p29_act_princ), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_cuidado_hogar_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p29_act_princ), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_cuidado_hogar_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p33_trabaj_como), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_sector_publico_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p33_trabaj_como), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_sector_publico_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p33_trabaj_como), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_sector_privado_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p33_trabaj_como), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_sector_privado_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p33_trabaj_como), ''), '0') AS UNSIGNED) = 3 THEN 1 ELSE 0 END) AS cant_empleo_domestico_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p33_trabaj_como), ''), '0') AS UNSIGNED) = 3 THEN 1 ELSE 0 END) AS cant_empleo_domestico_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 15 THEN 1 ELSE 0 END) AS poblacion_15_mas_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 15 THEN 1 ELSE 0 END) AS poblacion_15_mas_h "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.personas_rec p "
                + "    ON p.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadTraOcuOcupacion(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("cant_hombre"), rs.getInt("cant_mujer"),
                            rs.getInt("cant_p_ocupada_m"), rs.getInt("cant_p_ocupada_h"),
                            rs.getInt("cant_p_desocupada_m"), rs.getInt("cant_p_desocupada_h"),
                            rs.getInt("cant_retirada_edad_15_m"), rs.getInt("cant_retirada_edad_15_h"),
                            rs.getInt("cant_cuidado_hogar_m"), rs.getInt("cant_cuidado_hogar_h"),
                            rs.getInt("cant_sector_publico_m"), rs.getInt("cant_sector_publico_h"),
                            rs.getInt("cant_sector_privado_m"), rs.getInt("cant_sector_privado_h"),
                            rs.getInt("cant_empleo_domestico_m"), rs.getInt("cant_empleo_domestico_h"),
                            rs.getInt("poblacion_15_mas_m"), rs.getInt("poblacion_15_mas_h")
                    );
                }
            }
        }

        return new CalidadTraOcuOcupacion(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadTicAcceso calcularCalidadTicAcceso(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 5 "
                + "             AND (CAST(COALESCE(NULLIF(TRIM(p.h_p23a_uso_pc), ''), '0') AS UNSIGNED) = 1 "
                + "             OR CAST(COALESCE(NULLIF(TRIM(p.h_p23b_uso_table), ''), '0') AS UNSIGNED) = 1 "
                + "             OR CAST(COALESCE(NULLIF(TRIM(p.h_p23c_uso_cel), ''), '0') AS UNSIGNED) = 1 "
                + "             OR CAST(COALESCE(NULLIF(TRIM(p.h_p23d_uso_basico), ''), '0') AS UNSIGNED) = 1) THEN 1 ELSE 0 END) AS cant_una_tic_minimo, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 5 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p23a_uso_pc), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_con_compu, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 5 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p23b_uso_table), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_con_tablet, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 5 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p23c_uso_cel), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_con_celular, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 5 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p23d_uso_basico), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_con_tel_fijo, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 5 "
                + "             AND CAST(COALESCE(NULLIF(TRIM(p.h_p23e_uso_inter), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_tiene_internet, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 5 THEN 1 ELSE 0 END) AS poblacion_total "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.personas_rec p "
                + "    ON p.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadTicAcceso(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("cant_una_tic_minimo"), rs.getInt("cant_con_compu"),
                            rs.getInt("cant_con_tablet"), rs.getInt("cant_con_celular"),
                            rs.getInt("cant_con_tel_fijo"), rs.getInt("cant_tiene_internet"),
                            rs.getInt("poblacion_total")
                    );
                }
            }
        }

        return new CalidadTicAcceso(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadNacResOtroLugar calcularCalidadNacResOtroLugar(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p08_lugar_na), ''), '0') AS UNSIGNED) IN (2, 3) THEN 1 ELSE 0 END) AS cant_nac_otro_muni, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p08_lugar_na), ''), '0') AS UNSIGNED) = 4 THEN 1 ELSE 0 END) AS cant_nac_otro_pais, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p12_lugar_residencia), ''), '0') AS UNSIGNED) IN (2, 3) THEN 1 ELSE 0 END) AS cant_nac_5anios, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p08_lugar_na), ''), '0') AS UNSIGNED) IN (2, 3) AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_nac_otro_muni_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p08_lugar_na), ''), '0') AS UNSIGNED) IN (2, 3) AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_nac_otro_muni_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p08_lugar_na), ''), '0') AS UNSIGNED) = 4 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_nac_otro_pais_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p08_lugar_na), ''), '0') AS UNSIGNED) = 4 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_nac_otro_pais_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p12_lugar_residencia), ''), '0') AS UNSIGNED) IN (2, 3) AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_nac_5anios_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_p12_lugar_residencia), ''), '0') AS UNSIGNED) IN (2, 3) AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_nac_5anios_m "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.personas_rec p "
                + "    ON p.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadNacResOtroLugar(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("cant_nac_otro_muni"), rs.getInt("cant_nac_otro_pais"),
                            rs.getInt("cant_nac_5anios"), rs.getInt("cant_nac_otro_muni_h"),
                            rs.getInt("cant_nac_otro_muni_m"), rs.getInt("cant_nac_otro_pais_h"),
                            rs.getInt("cant_nac_otro_pais_m"), rs.getInt("cant_nac_5anios_h"),
                            rs.getInt("cant_nac_5anios_m")
                    );
                }
            }
        }

        return new CalidadNacResOtroLugar(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadCompHogEnvejecimiento calcularCalidadCompHogEnvejecimiento(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch06_rnp), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS no_inscrita_rnp_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch06_rnp), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS no_inscrita_rnp_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 60 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_mayor_60_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 60 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_mayor_60_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_hombre, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_mujer, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 100 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_100_mas_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 100 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_100_mas_m, "
                + "    COUNT(p.`personas_rec-id`) AS cantidad, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) < 15 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_menor_15_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) < 15 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_menor_15_m "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.personas_rec p "
                + "    ON p.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadCompHogEnvejecimiento(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("no_inscrita_rnp_h"), rs.getInt("no_inscrita_rnp_m"),
                            rs.getInt("cant_mayor_60_h"), rs.getInt("cant_mayor_60_m"),
                            rs.getInt("cant_hombre"), rs.getInt("cant_mujer"),
                            rs.getInt("cant_100_mas_h"), rs.getInt("cant_100_mas_m"),
                            rs.getInt("cantidad"), rs.getInt("cant_menor_15_h"),
                            rs.getInt("cant_menor_15_m")
                    );
                }
            }
        }

        return new CalidadCompHogEnvejecimiento(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadCompHogPiramide calcularCalidadCompHogPiramide(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 70 AND 74 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_70_74_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 70 AND 74 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_70_74_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 75 AND 79 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_75_79_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 75 AND 79 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_75_79_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 80 AND 84 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_80_84_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 80 AND 84 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_80_84_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 85 AND 89 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_85_89_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 85 AND 89 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_85_89_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 90 AND 94 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_90_94_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 90 AND 94 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_90_94_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 95 AND 99 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_95_99_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) BETWEEN 95 AND 99 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_95_99_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 100 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_100_mas_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) >= 100 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_100_mas_m "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.personas_rec p "
                + "    ON p.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadCompHogPiramide(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(),
                            rs.getInt("edad_70_74_h"), rs.getInt("edad_70_74_m"),
                            rs.getInt("edad_75_79_h"), rs.getInt("edad_75_79_m"),
                            rs.getInt("edad_80_84_h"), rs.getInt("edad_80_84_m"),
                            rs.getInt("edad_85_89_h"), rs.getInt("edad_85_89_m"),
                            rs.getInt("edad_90_94_h"), rs.getInt("edad_90_94_m"),
                            rs.getInt("edad_95_99_h"), rs.getInt("edad_95_99_m"),
                            rs.getInt("edad_100_mas_h"), rs.getInt("edad_100_mas_m")
                    );
                }
            }
        }

        return new CalidadCompHogPiramide(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(),
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CalidadConyEstado calcularCalidadConyEstado(LlaveCensista llave) throws Exception {

        String sql
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_mujer, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_hombre, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_casado_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cant_casado_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_union_libre_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS cant_union_libre_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 3 THEN 1 ELSE 0 END) AS cant_separado_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 3 THEN 1 ELSE 0 END) AS cant_separado_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 4 THEN 1 ELSE 0 END) AS cant_divorciado_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 4 THEN 1 ELSE 0 END) AS cant_divorciado_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 5 THEN 1 ELSE 0 END) AS cant_viudo_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 5 THEN 1 ELSE 0 END) AS cant_viudo_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 6 THEN 1 ELSE 0 END) AS cant_soltero_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 6 THEN 1 ELSE 0 END) AS cant_soltero_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) < 18 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS menor_18_casado_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) < 18 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS menor_18_casado_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) < 18 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS menor_18_union_libre_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) < 18 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS menor_18_union_libre_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) < 18 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 3 THEN 1 ELSE 0 END) AS menor_18_separado_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) < 18 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 3 THEN 1 ELSE 0 END) AS menor_18_separado_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) < 18 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 4 THEN 1 ELSE 0 END) AS menor_18_divorciado_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) < 18 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 AND CAST(COALESCE(NULLIF(TRIM(p.h_p17_esta_civil), ''), '0') AS UNSIGNED) = 4 THEN 1 ELSE 0 END) AS menor_18_divorciado_m, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) < 18 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS edad_menos_18_h, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(p.h_ch04_edad), ''), '0') AS UNSIGNED) < 18 AND CAST(COALESCE(NULLIF(TRIM(p.h_ch03_sexo), ''), '0') AS UNSIGNED) = 2 THEN 1 ELSE 0 END) AS edad_menos_18_m "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.personas_rec p "
                + "    ON p.`level-1-id` = l1.`level-1-id` "
                + "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, llave.getDepto());
            ps.setString(2, llave.getMuni());
            ps.setInt(3, llave.getZona());
            ps.setInt(4, llave.getSector());
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CalidadConyEstado(
                            llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                            llave.getSegmento(), llave.getCensista(),
                            rs.getInt("cant_mujer"), rs.getInt("cant_hombre"),
                            rs.getInt("cant_casado_h"), rs.getInt("cant_casado_m"),
                            rs.getInt("cant_union_libre_h"), rs.getInt("cant_union_libre_m"),
                            rs.getInt("cant_separado_h"), rs.getInt("cant_separado_m"),
                            rs.getInt("cant_divorciado_h"), rs.getInt("cant_divorciado_m"),
                            rs.getInt("cant_viudo_h"), rs.getInt("cant_viudo_m"),
                            rs.getInt("cant_soltero_h"), rs.getInt("cant_soltero_m"),
                            rs.getInt("menor_18_casado_h"), rs.getInt("menor_18_casado_m"),
                            rs.getInt("menor_18_union_libre_h"), rs.getInt("menor_18_union_libre_m"),
                            rs.getInt("menor_18_separado_h"), rs.getInt("menor_18_separado_m"),
                            rs.getInt("menor_18_divorciado_h"), rs.getInt("menor_18_divorciado_m"),
                            rs.getInt("edad_menos_18_h"), rs.getInt("edad_menos_18_m")
                    );
                }
            }
        }

        return new CalidadConyEstado(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        );
    }

    public CobCensistaProductividad calcularCobCensistaProductividad(LlaveCensista llave) throws Exception {

        int estructurasTrabajadas = 0;
        int totalViviendasParticulares = 0;
        int viviendasDesocupadas = 0;
        int viviendasOcupAusentes = 0;
        int rechazos = 0;
        int cuestionariosEfectivos = 0;
        int cantidadVisitas = 0;
        int diasTrabajados = 0;
        int promedioDuracionSeg = 0;
        int maxDuracionSeg = 0;
        int cantidadHogaresUnipersonales = 0;
        int cantidadHogares = 0;
        int area = 0;

        String whereLlave
                = "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        // 1) estructuras trabajadas: solo level-1, sin joins
        String sqlEstructuras
                = "SELECT COUNT(DISTINCT TRIM(l1.l1_estructura)) AS estructuras_trabajadas "
                + "FROM cnpv_data.`level-1` l1 "
                + whereLlave;

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sqlEstructuras)) {
            bindLlave(ps, llave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    estructurasTrabajadas = rs.getInt("estructuras_trabajadas");
                }
            }
        }

        // 2) vivienda_rec: total de viviendas particulares (excluye colectivas 8-12)
        String sqlViviendaParticular
                = "SELECT SUM(CASE WHEN (CASE "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa Independiente' THEN 1 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Apartamento' THEN 2 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Cuarto en meson o cuarteria' THEN 3 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Local no construido para vivienda' THEN 4 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Rancho (de materiales naturales)' THEN 5 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa improvisada (material de desecho)' THEN 6 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Otro tipo de vivienda particular' THEN 7 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) IN ('Hotel, pensión, casa de huéspedes', 'Hospital, sanatorio o clínica', 'Orfanato', 'Asilo', 'Cuartel, batallón o posta policial') THEN 8 "
                + "        ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v01_tipo_viv), ''), '0') AS UNSIGNED) END) BETWEEN 1 AND 7 "
                + "        THEN 1 ELSE 0 END) AS total_viviendas_particulares "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.vivienda_rec vr ON vr.`level-1-id` = l1.`level-1-id` "
                + whereLlave;

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sqlViviendaParticular)) {
            bindLlave(ps, llave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalViviendasParticulares = rs.getInt("total_viviendas_particulares");
                }
            }
        }

        // 3) visita: desocupadas, ausentes, rechazos, cuestionarios efectivos, total visitas, dias trabajados
        String sqlVisita
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 5 THEN 1 ELSE 0 END) AS desocupadas, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 4 THEN 1 ELSE 0 END) AS ocup_ausentes, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) IN (2, 3) THEN 1 ELSE 0 END) AS rechazos, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS cuestionarios_efectivos, "
                + "    COUNT(v.`visita-id`) AS cantidad_visitas, "
                + "    COUNT(DISTINCT STR_TO_DATE(v.h_fvisita, '%d-%m-%Y')) AS dias_trabajados "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.visita v ON v.`level-1-id` = l1.`level-1-id` "
                + whereLlave;

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sqlVisita)) {
            bindLlave(ps, llave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    viviendasDesocupadas = rs.getInt("desocupadas");
                    viviendasOcupAusentes = rs.getInt("ocup_ausentes");
                    rechazos = rs.getInt("rechazos");
                    cuestionariosEfectivos = rs.getInt("cuestionarios_efectivos");
                    cantidadVisitas = rs.getInt("cantidad_visitas");
                    diasTrabajados = rs.getInt("dias_trabajados");
                }
            }
        }

        // 4) metadatos_rec: duracion de entrevista (epoch fin - epoch inicio, en segundos)
        String sqlDuracion
                = "SELECT "
                + "    ROUND(AVG(CASE WHEN CAST(COALESCE(NULLIF(TRIM(m.h_end_interview_time), ''), '0') AS UNSIGNED) "
                + "                   > CAST(COALESCE(NULLIF(TRIM(m.h_start_interview_time), ''), '0') AS UNSIGNED) "
                + "        THEN CAST(COALESCE(NULLIF(TRIM(m.h_end_interview_time), ''), '0') AS UNSIGNED) "
                + "           - CAST(COALESCE(NULLIF(TRIM(m.h_start_interview_time), ''), '0') AS UNSIGNED) "
                + "        ELSE NULL END)) AS promedio_duracion_seg, "
                + "    MAX(CASE WHEN CAST(COALESCE(NULLIF(TRIM(m.h_end_interview_time), ''), '0') AS UNSIGNED) "
                + "                   > CAST(COALESCE(NULLIF(TRIM(m.h_start_interview_time), ''), '0') AS UNSIGNED) "
                + "        THEN CAST(COALESCE(NULLIF(TRIM(m.h_end_interview_time), ''), '0') AS UNSIGNED) "
                + "           - CAST(COALESCE(NULLIF(TRIM(m.h_start_interview_time), ''), '0') AS UNSIGNED) "
                + "        ELSE NULL END) AS max_duracion_seg "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.metadatos_rec m ON m.`level-1-id` = l1.`level-1-id` "
                + whereLlave;

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sqlDuracion)) {
            bindLlave(ps, llave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    promedioDuracionSeg = rs.getInt("promedio_duracion_seg");
                    maxDuracionSeg = rs.getInt("max_duracion_seg");
                }
            }
        }

        // 5) hogares_rec: hogares unipersonales
        String sqlHogares
                = "SELECT "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(hr.h_ch00_num_per), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS unipersonales, "
                + "    COUNT(hr.`hogares_rec-id`) AS cantidad_hogares "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.hogares_rec hr ON hr.`level-1-id` = l1.`level-1-id` "
                + whereLlave;

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sqlHogares)) {
            bindLlave(ps, llave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cantidadHogaresUnipersonales = rs.getInt("unipersonales");
                    cantidadHogares = rs.getInt("cantidad_hogares");
                }
            }
        }

        // 6) metadatos_rec: area (urbano/rural) - se toma el valor mas frecuente del grupo
        String sqlArea
                = "SELECT CASE WHEN CAST(COALESCE(NULLIF(TRIM(m.h_ur_area), ''), '0') AS SIGNED) IN (1, 2) "
                + "        THEN CAST(COALESCE(NULLIF(TRIM(m.h_ur_area), ''), '0') AS SIGNED) ELSE 0 END AS area, "
                + "    COUNT(*) AS n "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.metadatos_rec m ON m.`level-1-id` = l1.`level-1-id` "
                + whereLlave
                + " GROUP BY area "
                + "ORDER BY n DESC LIMIT 1";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sqlArea)) {
            bindLlave(ps, llave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    area = rs.getInt("area");
                }
            }
        }

        return new CobCensistaProductividad(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(),
                estructurasTrabajadas, totalViviendasParticulares,
                viviendasDesocupadas, viviendasOcupAusentes, rechazos, cuestionariosEfectivos,
                cantidadVisitas, promedioDuracionSeg, maxDuracionSeg, diasTrabajados,
                cantidadHogaresUnipersonales, cantidadHogares, area
        );
    }

    public CobCensistaPorVivienda calcularCobCensistaPorVivienda(LlaveCensista llave) throws Exception {

        int totalViviendasParticulares = 0;
        int totalViviendasRealizadas = 0;
        int personasAusentes = 0;
        int viviendasConRechazo = 0;
        int boletasEfectivas = 0;
        int area = 0;

        String whereLlave
                = "WHERE LPAD(TRIM(l1.l1_departamento), 2, '0') = ? "
                + "  AND LPAD(TRIM(l1.l1_municipio), 2, '0') = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_zona), ''), '0') AS UNSIGNED) = ? "
                + "  AND CAST(COALESCE(NULLIF(TRIM(l1.l1_sector), ''), '0') AS UNSIGNED) = ? "
                + "  AND TRIM(l1.l1_segmento) = ? "
                + "  AND TRIM(l1.l1_cod_encuestador) = ?";

        // 1) vivienda_rec: total de viviendas particulares (excluye colectivas 8-12)
        String sqlViviendaParticular
                = "SELECT SUM(CASE WHEN (CASE "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa Independiente' THEN 1 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Apartamento' THEN 2 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Cuarto en meson o cuarteria' THEN 3 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Local no construido para vivienda' THEN 4 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Rancho (de materiales naturales)' THEN 5 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Casa improvisada (material de desecho)' THEN 6 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) = 'Otro tipo de vivienda particular' THEN 7 "
                + "        WHEN TRIM(vr.h_v01_tipo_viv) IN ('Hotel, pensión, casa de huéspedes', 'Hospital, sanatorio o clínica', 'Orfanato', 'Asilo', 'Cuartel, batallón o posta policial') THEN 8 "
                + "        ELSE CAST(COALESCE(NULLIF(TRIM(vr.h_v01_tipo_viv), ''), '0') AS UNSIGNED) END) BETWEEN 1 AND 7 "
                + "        THEN 1 ELSE 0 END) AS total_viviendas_particulares "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.vivienda_rec vr ON vr.`level-1-id` = l1.`level-1-id` "
                + whereLlave;

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sqlViviendaParticular)) {
            bindLlave(ps, llave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalViviendasParticulares = rs.getInt("total_viviendas_particulares");
                }
            }
        }

        // 2) visita: realizadas, ausentes, rechazo, efectivas
        String sqlVisita
                = "SELECT "
                + "    COUNT(v.`visita-id`) AS realizadas, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 4 THEN 1 ELSE 0 END) AS ausentes, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) IN (2, 3) THEN 1 ELSE 0 END) AS rechazo, "
                + "    SUM(CASE WHEN CAST(COALESCE(NULLIF(TRIM(v.h_rvisita), ''), '0') AS UNSIGNED) = 1 THEN 1 ELSE 0 END) AS efectivas "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.visita v ON v.`level-1-id` = l1.`level-1-id` "
                + whereLlave;

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sqlVisita)) {
            bindLlave(ps, llave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalViviendasRealizadas = rs.getInt("realizadas");
                    personasAusentes = rs.getInt("ausentes");
                    viviendasConRechazo = rs.getInt("rechazo");
                    boletasEfectivas = rs.getInt("efectivas");
                }
            }
        }

        // 3) metadatos_rec: area (urbano/rural) - se toma el valor mas frecuente del grupo
        String sqlArea
                = "SELECT CASE WHEN CAST(COALESCE(NULLIF(TRIM(m.h_ur_area), ''), '0') AS SIGNED) IN (1, 2) "
                + "        THEN CAST(COALESCE(NULLIF(TRIM(m.h_ur_area), ''), '0') AS SIGNED) ELSE 0 END AS area, "
                + "    COUNT(*) AS n "
                + "FROM cnpv_data.`level-1` l1 "
                + "LEFT JOIN cnpv_data.metadatos_rec m ON m.`level-1-id` = l1.`level-1-id` "
                + whereLlave
                + " GROUP BY area "
                + "ORDER BY n DESC LIMIT 1";

        try (
                Connection con = DataSourceFactory.getOrigenConnection();
                PreparedStatement ps = con.prepareStatement(sqlArea)) {
            bindLlave(ps, llave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    area = rs.getInt("area");
                }
            }
        }

        // "Viviendas transformadas" queda pendiente: no hay codigo confirmado en h_rvisita/h_control para esto todavia.
        int viviendasTransformadas = 0;

        // "Numero de entrevistas rechazadas" es el mismo concepto que "viviendas con rechazo" (h_rvisita IN (2,3)).
        int entrevistasRechazadas = viviendasConRechazo;

        return new CobCensistaPorVivienda(
                llave.getDepto(), llave.getMuni(), llave.getApoyoMunicipal(), llave.getZona(), llave.getSector(),
                llave.getSegmento(), llave.getCensista(), area,
                totalViviendasParticulares, totalViviendasRealizadas, personasAusentes,
                viviendasConRechazo, viviendasTransformadas, boletasEfectivas, entrevistasRechazadas
        );
    }

    private void bindLlave(PreparedStatement ps, LlaveCensista llave) throws Exception {
        ps.setString(1, llave.getDepto());
        ps.setString(2, llave.getMuni());
        ps.setInt(3, llave.getZona());
        ps.setInt(4, llave.getSector());
        ps.setString(5, llave.getSegmento());
        ps.setString(6, llave.getCensista());
    }

}
