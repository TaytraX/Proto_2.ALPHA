package Engine;

import Entity.AnimationState;
import Entity.Camera;
import Entity.Player;
import Entity.PlayerState;
import Laucher.Main;
import Render.Window;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class Engine {
    public Window window;
    public boolean isRunning;
    public Renderer Renderer;
    public PlayerState playerState;
    public Camera camera;
    public Player player;
    private Physics physics;
    private long lastTime = System.currentTimeMillis();
    private float deltaTime = 0.016f;

    public List<AABB> grounds = new ArrayList<>();

    public void start() {
        init();
        run();
    }

    private void init() {
        try {
            window = Main.getWindow();
            if(window == null) throw new Exception("Window nor initialized");
            Renderer = new Renderer();
            playerState = ThreadManager.playerState.get();
            camera = new Camera();
            player = new Player();
            physics = new Physics();

            // Initialiser un PlayerState de base
            PlayerState initialState = new PlayerState(
                    new Vector2f(0, 0),
                    new Vector2f(0, 0),
                    false,
                    AnimationState.IDLE,
                    true,
                    false,
                    false,
                    false,
                    5.0f,
                    10.0f,
                    System.currentTimeMillis()
            );
            ThreadManager.playerState.set(initialState);
            grounds.add(new AABB(new Vector2f(0, -5), new Vector2f(10, 1)));

            window.init();
            Renderer.initialize();
        } catch (Exception e) {
            System.err.println("Failed to initialize engine : " + e.getMessage());
        }
    }

    public void render() {
        try {
            window.clear();
            Renderer.renderFrame(camera, deltaTime);
            window.update();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void run() {
        isRunning = true;
            while (isRunning && !window.windowShouldClose()) {
                long currentTime = System.currentTimeMillis();
                deltaTime = (currentTime - lastTime) / 1000f;
                lastTime = currentTime;

                deltaTime = Math.min(deltaTime, 0.016f);

                handleInput();
                update();
                render();
            }
        cleanup();
    }

    private void handleInput() {
        PlayerState currentState = ThreadManager.playerState.get();
        PlayerState inputState = player.input(currentState);
        ThreadManager.playerState.set(inputState);
    }

    public void update() {
        PlayerState currentState = ThreadManager.playerState.get();
        PlayerState physicsState = physics.update(currentState, deltaTime, grounds);
        ThreadManager.playerState.set(physicsState);

        window.clear();
    }

    public void cleanup() {
        Renderer.cleanUp();
        window.cleanup();
        glfwTerminate();
    }
}