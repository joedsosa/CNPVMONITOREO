package hn.gob.ine.listener.dao;

import hn.gob.ine.listener.config.DataSourceFactory;
import hn.gob.ine.listener.model.LlaveCensista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ControlCorteDAO {

    public int crearEjecucion(int idConfigCorte) throws Exception {

        String sql =
                "INSERT INTO listener_censo_control.ejecucion_corte " +
                "(id_config_corte, fecha_corte, hora_inicio, estado) " +
                "VALUES (?, CURDATE(), NOW(), 'EN_PROCESO')";

        try (
                Connection con = DataSourceFactory.getControlConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, idConfigCorte);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new RuntimeException("No se pudo crear la ejecucion del corte");
    }

    public boolean censistaFinalizado(int idEjecucion, LlaveCensista llave) throws Exception {

        String sql =
                "SELECT COUNT(*) AS total " +
                "FROM listener_censo_control.control_censista_corte " +
                "WHERE id_ejecucion = ? " +
                "AND depto = ? " +
                "AND muni = ? " +
                "AND zona = ? " +
                "AND sector = ? " +
                "AND segmento = ? " +
                "AND censista = ? " +
                "AND estado = 'FINALIZADO'";

        try (
                Connection con = DataSourceFactory.getControlConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, idEjecucion);
            ps.setString(2, llave.getDepto());
            ps.setString(3, llave.getMuni());
            ps.setInt(4, llave.getZona());
            ps.setInt(5, llave.getSector());
            ps.setString(6, llave.getSegmento());
            ps.setString(7, llave.getCensista());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        }

        return false;
    }

    public void marcarProcesando(int idEjecucion, LlaveCensista llave) throws Exception {

        String sql =
                "INSERT INTO listener_censo_control.control_censista_corte " +
                "(id_ejecucion, depto, muni, zona, sector, segmento, censista, estado, hora_inicio) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'PROCESANDO', NOW()) " +
                "ON DUPLICATE KEY UPDATE " +
                "estado = 'PROCESANDO', " +
                "hora_inicio = NOW(), " +
                "mensaje_error = NULL";

        try (
                Connection con = DataSourceFactory.getControlConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, idEjecucion);
            ps.setString(2, llave.getDepto());
            ps.setString(3, llave.getMuni());
            ps.setInt(4, llave.getZona());
            ps.setInt(5, llave.getSector());
            ps.setString(6, llave.getSegmento());
            ps.setString(7, llave.getCensista());

            ps.executeUpdate();
        }
    }

    public void marcarFinalizado(int idEjecucion, LlaveCensista llave) throws Exception {

        String sql =
                "UPDATE listener_censo_control.control_censista_corte " +
                "SET estado = 'FINALIZADO', hora_fin = NOW(), mensaje_error = NULL " +
                "WHERE id_ejecucion = ? " +
                "AND depto = ? " +
                "AND muni = ? " +
                "AND zona = ? " +
                "AND sector = ? " +
                "AND segmento = ? " +
                "AND censista = ?";

        try (
                Connection con = DataSourceFactory.getControlConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, idEjecucion);
            ps.setString(2, llave.getDepto());
            ps.setString(3, llave.getMuni());
            ps.setInt(4, llave.getZona());
            ps.setInt(5, llave.getSector());
            ps.setString(6, llave.getSegmento());
            ps.setString(7, llave.getCensista());

            ps.executeUpdate();
        }
    }

    public void marcarError(int idEjecucion, LlaveCensista llave, String mensaje) throws Exception {

        String sql =
                "UPDATE listener_censo_control.control_censista_corte " +
                "SET estado = 'ERROR', hora_fin = NOW(), mensaje_error = ? " +
                "WHERE id_ejecucion = ? " +
                "AND depto = ? " +
                "AND muni = ? " +
                "AND zona = ? " +
                "AND sector = ? " +
                "AND segmento = ? " +
                "AND censista = ?";

        try (
                Connection con = DataSourceFactory.getControlConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, mensaje);
            ps.setInt(2, idEjecucion);
            ps.setString(3, llave.getDepto());
            ps.setString(4, llave.getMuni());
            ps.setInt(5, llave.getZona());
            ps.setInt(6, llave.getSector());
            ps.setString(7, llave.getSegmento());
            ps.setString(8, llave.getCensista());

            ps.executeUpdate();
        }
    }

    public void finalizarEjecucion(int idEjecucion, String estado) throws Exception {

        String sql =
                "UPDATE listener_censo_control.ejecucion_corte " +
                "SET estado = ?, hora_fin = NOW(), " +
                "total_censistas = (" +
                "   SELECT COUNT(*) " +
                "   FROM listener_censo_control.control_censista_corte " +
                "   WHERE id_ejecucion = ?" +
                "), " +
                "total_procesados = (" +
                "   SELECT COUNT(*) " +
                "   FROM listener_censo_control.control_censista_corte " +
                "   WHERE id_ejecucion = ? " +
                "   AND estado = 'FINALIZADO'" +
                "), " +
                "total_error = (" +
                "   SELECT COUNT(*) " +
                "   FROM listener_censo_control.control_censista_corte " +
                "   WHERE id_ejecucion = ? " +
                "   AND estado = 'ERROR'" +
                ") " +
                "WHERE id = ?";

        try (
                Connection con = DataSourceFactory.getControlConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, estado);
            ps.setInt(2, idEjecucion);
            ps.setInt(3, idEjecucion);
            ps.setInt(4, idEjecucion);
            ps.setInt(5, idEjecucion);

            ps.executeUpdate();
        }
    }

    public void registrarErrorPaso(int idEjecucion, LlaveCensista llave, String tablaDestino, String mensajeError) throws Exception {

        String sql =
                "INSERT INTO listener_censo_control.log_error_corte " +
                "(id_ejecucion, depto, muni, sector, segmento, censista, tabla_destino, mensaje_error, fecha_error) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";

        try (
                Connection con = DataSourceFactory.getControlConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, idEjecucion);
            ps.setString(2, llave.getDepto());
            ps.setString(3, llave.getMuni());
            ps.setString(4, String.valueOf(llave.getSector()));
            ps.setString(5, llave.getSegmento());
            ps.setString(6, llave.getCensista());
            ps.setString(7, tablaDestino);
            ps.setString(8, mensajeError);

            ps.executeUpdate();
        }
    }

    public boolean existeEjecucionHoy(int idConfigCorte) throws Exception {

        String sql =
                "SELECT COUNT(*) AS total " +
                "FROM listener_censo_control.ejecucion_corte " +
                "WHERE id_config_corte = ? " +
                "AND fecha_corte = CURDATE() " +
                "AND estado IN ('EN_PROCESO', 'FINALIZADO', 'FINALIZADO_CON_TIMEOUT')";

        try (
                Connection con = DataSourceFactory.getControlConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, idConfigCorte);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        }

        return false;
    }
}