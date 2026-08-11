package hn.gob.ine.listener.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceFactory {

    private static HikariDataSource origenDataSource;
    private static HikariDataSource destinoDataSource;
    private static HikariDataSource controlDataSource;

    private DataSourceFactory() {
    }

    private static HikariDataSource crearDataSource(String prefijo, String nombrePool) {

        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(DatabaseConfig.get(prefijo + ".url"));
        config.setUsername(DatabaseConfig.get(prefijo + ".user"));
        config.setPassword(DatabaseConfig.get(prefijo + ".password"));

        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        config.setMaximumPoolSize(DatabaseConfig.getInt("db.pool.maxSize"));
        config.setMinimumIdle(DatabaseConfig.getInt("db.pool.minIdle"));
        config.setConnectionTimeout(DatabaseConfig.getInt("db.pool.connectionTimeout"));

        config.setPoolName(nombrePool);

        return new HikariDataSource(config);
    }

    public static synchronized HikariDataSource getOrigenDataSource() {
        if (origenDataSource == null) {
            origenDataSource = crearDataSource("db.origen", "POOL_CNPV_DATA");
        }
        return origenDataSource;
    }

    public static synchronized HikariDataSource getDestinoDataSource() {
        if (destinoDataSource == null) {
            destinoDataSource = crearDataSource("db.destino", "POOL_CENSO_MONITOREO");
        }
        return destinoDataSource;
    }

    public static synchronized HikariDataSource getControlDataSource() {
        if (controlDataSource == null) {
            controlDataSource = crearDataSource("db.control", "POOL_LISTENER_CONTROL");
        }
        return controlDataSource;
    }

    public static Connection getOrigenConnection() throws SQLException {
        return getOrigenDataSource().getConnection();
    }

    public static Connection getDestinoConnection() throws SQLException {
        return getDestinoDataSource().getConnection();
    }

    public static Connection getControlConnection() throws SQLException {
        return getControlDataSource().getConnection();
    }

    public static void cerrarTodo() {
        if (origenDataSource != null) {
            origenDataSource.close();
        }

        if (destinoDataSource != null) {
            destinoDataSource.close();
        }

        if (controlDataSource != null) {
            controlDataSource.close();
        }
    }
}