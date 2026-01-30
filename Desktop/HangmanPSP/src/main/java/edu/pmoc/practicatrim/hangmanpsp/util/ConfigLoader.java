package edu.pmoc.practicatrim.hangmanpsp.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static final Properties props = new Properties();

    static {

        try (InputStream is = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) {

                throw new RuntimeException("Error: No se encuentra el archivo config.properties en resources");
            }
            props.load(is);
            System.out.println("Configuración cargada correctamente.");
        } catch (IOException e) {
            throw new RuntimeException("Error al leer config.properties", e);
        }
    }

    public static String getProperty(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            System.err.println("Advertencia: La propiedad [" + key + "] no existe en el config.properties");
        }
        return value;
    }

    public static int getIntProperty(String key) {
        return Integer.parseInt(getProperty(key).trim());
    }
}