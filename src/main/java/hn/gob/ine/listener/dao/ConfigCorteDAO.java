package hn.gob.ine.listener.dao;

import hn.gob.ine.listener.config.DataSourceFactory;
import hn.gob.ine.listener.model.CorteConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConfigCorteDAO {

    public List<CorteConfig> obtenerCortesActivos() throws Exception {

        List<CorteConfig> lista = new ArrayList<CorteConfig>();

        String sql =
                "SELECT id, nombre, activo, hora_corte, descripcion " +
                "FROM listener_censo_control.config_cortes " +
                "WHERE activo = 1 " +
                "ORDER BY hora_corte";

        try (
                Connection con = DataSourceFactory.getControlConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                CorteConfig corte = new CorteConfig(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getBoolean("activo"),
                        rs.getTime("hora_corte"),
                        rs.getString("descripcion")
                );

                lista.add(corte);
            }
        }

        return lista;
    }
}