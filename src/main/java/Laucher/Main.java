package Laucher;

import Render.Window;
import static org.lwjgl.glfw.GLFW.*;

public class Main {

    public static Window window;

    public static void main(String[] args) {
        // Créer la fenêtre
        window = new Window("Minecraft 2D", 1280, 720, true);

        // Initialiser
        window.init();

        // Boucle principale
        while (!window.windowShouldClose()) {
            window.clear();
            window.update();
        }

        // Nettoyage
        window.cleanup();
        glfwTerminate();
    }

    public static Window getWindow() {
        return window;
    }
}