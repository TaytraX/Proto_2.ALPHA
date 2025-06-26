package Laucher;

import Render.Window;
import static org.lwjgl.glfw.GLFW.*;

public class Main {

    public static void main(String[] args) {
        // Créer la fenêtre
        Window window = new Window("Minecraft 2D", 1280, 720, true);

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
}