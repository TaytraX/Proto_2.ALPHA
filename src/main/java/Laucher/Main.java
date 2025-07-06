package Laucher;

import Render.Window;
import static org.lwjgl.glfw.GLFW.*;

public class Main {

    public static Window window;

    public static int width = 1280;
    public static int height = 800;

    public static void main(String[] args) {
        // Créer la fenêtre
        window = new Window("2D Platformer", width, height, true);

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

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }
}