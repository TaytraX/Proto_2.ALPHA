package Engine;

import Entity.AnimationState;
import Entity.Camera;
import Entity.Player;
import Entity.PlayerState;
import Laucher.Main;
import Render.Window;
import org.joml.Vector2f;

import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class Engine {
    public static boolean isRunning;
    public Window window;
    public Renderer Renderer;
    public PlayerState playerState;
    public Camera camera;
    public Player player;
    private Physics physics;
    float deltaTime = 16.0f;

    public List<AABB> grounds;

    public void start() {
        init();
        if(isRunning) return;
        run();
    }

    private void init() {
        playerState = ThreadManager.playerState.get();
        window = Main.getWindow();
        Renderer = new Renderer();
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

        window.init();
        Renderer.initialize();

    }

    public void render() {
    try {
        Renderer.renderFrame(camera, deltaTime);
        window.update();
    } catch (Exception e) { e.printStackTrace(); }
    }

    public void run() {
        isRunning = true;
            while (isRunning) {
                try{
                handleInput();
                update();
                render();
                }catch (Exception e) {
                    e.printStackTrace();
                }
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