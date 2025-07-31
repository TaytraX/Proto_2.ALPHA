package laucher;

import engine.Engine;
import render.Window;

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

    public static Engine getEngine() {
        return engine;
    }

    public static Window getWindow() {
        return window;
    }
}