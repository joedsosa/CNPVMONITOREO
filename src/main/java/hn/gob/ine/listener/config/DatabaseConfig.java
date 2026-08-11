package hn.gob.ine.listener.config;

import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties properties = new Properties();

    static {
        try {
            InputStream input = DatabaseConfig.class
                    .getClassLoader()
                    .getResourceAsStream("application.properties");

            if (input == null) {
                throw new RuntimeException("No se encontro el archivo application.properties");
            }

            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Error cargando application.properties: " + e.getMessage(), e);
        }
    }

    private DatabaseConfig() {
    }

    public static String get(String key) {

        String nombreVariableEntorno = key.toUpperCase().replace('.', '_');
        String valorEntorno = System.getenv(nombreVariableEntorno);

        if (valorEntorno != null && !valorEntorno.trim().isEmpty()) {
            return valorEntorno.trim();
        }

        String value = properties.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("No existe la propiedad: " + key);
        }

        return value.trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}