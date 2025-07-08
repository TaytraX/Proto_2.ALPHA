package Laucher;

import Engine.Engine;
import Render.Window;

public class Main {

    public static Window window;
    public static Engine engine;

    public static int width = 1280;
    public static int height = 800;

    public static void main(String[] args) {
        // Créer la fenêtre
        window = new Window("2D Platformer", width, height, true);
        engine = new Engine();

        try {
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
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