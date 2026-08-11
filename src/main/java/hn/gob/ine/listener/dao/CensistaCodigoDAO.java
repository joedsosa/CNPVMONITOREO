package hn.gob.ine.listener.dao;

import hn.gob.ine.listener.config.DataSourceFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CensistaCodigoDAO {

    public String obtenerOCrearCodigo(String depto, String muni, String censistaOrigen) throws Exception {

        String codigoExistente = buscarCodigo(depto, muni, censistaOrigen);

        if (codigoExistente != null) {
            return codigoExistente;
        }

        synchronized (CensistaCodigoDAO.class) {
            codigoExistente = buscarCodigo(depto, muni, censistaOrigen);

            if (codigoExistente != null) {
                return codigoExistente;
            }

            String nuevoCodigo = generarSiguienteCodigo(depto, muni);
            insertarCodigo(depto, muni, censistaOrigen, nuevoCodigo);

            return nuevoCodigo;
        }
    }

    private String buscarCodigo(String depto, String muni, String censistaOrigen) throws Exception {

        String sql =
                "SELECT codigo_censista " +
                "FROM listener_censo_control.censista_codigo " +
                "WHERE depto = ? " +
                "AND muni = ? " +
                "AND censista_origen = ?";

        try (
                Connection con = DataSourceFactory.getControlConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, depto);
            ps.setString(2, muni);
            ps.setString(3, censistaOrigen);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("codigo_censista");
                }
            }
        }

        return null;
    }

    private String generarSiguienteCodigo(String depto, String muni) throws Exception {

        String sql =
                "SELECT IFNULL(MAX(CAST(SUBSTRING(codigo_censista, 6, 4) AS UNSIGNED)), 0) + 1 AS siguiente " +
                "FROM listener_censo_control.censista_codigo " +
                "WHERE depto = ? " +
                "AND muni = ?";

        int siguiente = 1;

        try (
                Connection con = DataSourceFactory.getControlConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, depto);
            ps.setString(2, muni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    siguiente = rs.getInt("siguiente");
                }
            }
        }

        return "C" + depto + muni + String.format("%04d", siguiente);
    }

    private void insertarCodigo(String depto, String muni, String censistaOrigen, String codigoCensista) throws Exception {

        String sql =
                "INSERT INTO listener_censo_control.censista_codigo " +
                "(depto, muni, censista_origen, codigo_censista) " +
                "VALUES (?, ?, ?, ?)";

        try (
                Connection con = DataSourceFactory.getControlConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, depto);
            ps.setString(2, muni);
            ps.setString(3, censistaOrigen);
            ps.setString(4, codigoCensista);

            ps.executeUpdate();
        }
    }
}