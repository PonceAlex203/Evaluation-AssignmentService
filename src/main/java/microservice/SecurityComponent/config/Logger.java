package microservice.SecurityComponent.config;

import java.time.LocalDateTime;

/**
 * Clase utilitaria para registrar mensajes en la consola con diferentes niveles de severidad
 * (INFO, SUCCESS, WARN, ERROR).
 * Los mensajes se imprimen con colores y marca de tiempo para facilitar la lectura en consola.
 */
public class Logger {

    // Códigos ANSI para colores de texto en consola
    public static final String ANSI_RESET  = "\u001B[0m";
    public static final String ANSI_RED    = "\u001B[31m";
    public static final String ANSI_GREEN  = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE   = "\u001B[34m";

    // Constructor privado para evitar la instanciación de la clase (solo métodos estáticos)
    private Logger() {}

    /**
     * Genera una marca de tiempo actual en formato ISO-8601.
     *
     * @return cadena con la fecha y hora actual.
     */
    private static String timestamp() {
        return LocalDateTime.now().toString();
    }

    /**
     * Registra un mensaje informativo (color azul).
     *
     * @param source clase desde donde se invoca el log
     * @param message mensaje a mostrar
     */
    public static void info(Class<?> source, String message) {
        System.out.println(ANSI_BLUE + "[INFO][" + timestamp() + "][" + source.getSimpleName() + "] "
                + ANSI_RESET + message);
    }

    /**
     * Registra un mensaje de éxito (color verde).
     *
     * @param source clase desde donde se invoca el log
     * @param message mensaje a mostrar
     */
    public static void success(Class<?> source, String message) {
        System.out.println(ANSI_GREEN + "[SUCCESS][" + timestamp() + "][" + source.getSimpleName() + "] "
                + ANSI_RESET + message);
    }

    /**
     * Registra una advertencia (color amarillo).
     *
     * @param source clase desde donde se invoca el log
     * @param message mensaje a mostrar
     */
    public static void warn(Class<?> source, String message) {
        System.out.println(ANSI_YELLOW + "[WARN][" + timestamp() + "][" + source.getSimpleName() + "] "
                + message + ANSI_RESET);
    }

    /**
     * Registra un mensaje de error (color rojo).
     *
     * @param source clase desde donde se invoca el log
     * @param message mensaje a mostrar
     */
    public static void error(Class<?> source, String message) {
        System.out.println(ANSI_RED + "[ERROR][" + timestamp() + "][" + source.getSimpleName() + "] "
                + message + ANSI_RESET);
    }
}
