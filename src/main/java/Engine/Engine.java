package Engine;

import Entity.Camera;
import Entity.PlayerState;
import Laucher.Main;
import Render.Window;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class Engine {
    public static boolean isRunning;
    private GLFWErrorCallback errorCallback;
    public Window window;
    public Renderer Renderer;
    public PlayerState playerState;
    public Camera camera;

    public void start() {
        init();
        if(isRunning) return;
        run();
    }

    private void init() {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Main.getWindow();
        Renderer = new Renderer(window);
        camera = new Camera();

        Renderer.initialize();

    }

    public void render() {
    try {
        GLFW.glfwPollEvents();
        Renderer.renderFrame(camera, 0.1f);
    } catch (Exception e) { e.printStackTrace(); }
    }

    public void run() {
        isRunning = true;
        try{
            while (isRunning) {
                render();
                update();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        cleanup();

    }

    public void update() {
        window.clear();
        window.update();
    }

    public void cleanup() {
        Renderer.cleanUp();
        window.cleanup();
        glfwTerminate();

    }
}