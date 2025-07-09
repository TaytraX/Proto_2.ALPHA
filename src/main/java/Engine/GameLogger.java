package Engine;

public class GameLogger {
    private static final boolean DEBUG = true;

    public static void info(String message) {
        if (DEBUG) System.out.println("[INFO] " + message);
    }

    public static void error(String message, Exception e) {
        System.err.println("[ERROR] " + message);
        if (e != null) e.printStackTrace();
    }

    public static void debug(String message) {
        if (DEBUG) System.out.println("[DEBUG] " + message);
    }
}