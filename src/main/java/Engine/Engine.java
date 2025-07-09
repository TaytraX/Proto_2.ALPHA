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
        GameLogger.info("Initialisation du moteur...");

        try {
            // Vérification critique
            window = Main.getWindow();
            if(window == null) {
                GameLogger.error("Window est null !", null);
                throw new Exception("Window not initialized");
            }
            GameLogger.info("Window OK");


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
                    4.0f,
                    8.0f,
                    System.currentTimeMillis()
            );
            ThreadManager.playerState.set(initialState);
            GameLogger.info("PlayerState initialisé");

            // Vérification que l'état est bien défini
            PlayerState test = ThreadManager.playerState.get();
            if (test == null) {
                GameLogger.error("PlayerState est null après initialisation !", null);
                throw new Exception("PlayerState initialization failed");
            }

            window.init();
            Renderer = new Renderer();
            Renderer.initialize();
            playerState = ThreadManager.playerState.get();
            camera = new Camera();
            player = new Player();
            physics = new Physics();

            // Dans init()
            grounds.add(new AABB(new Vector2f(0, -5), new Vector2f(10, 1)));    // Sol principal
            grounds.add(new AABB(new Vector2f(5, -2), new Vector2f(3, 0.5f)));  // Plateforme
            grounds.add(new AABB(new Vector2f(-5, 0), new Vector2f(2, 0.5f)));  // Autre plateforme

            GameLogger.info("Composants créés");

        } catch (Exception e) {
            GameLogger.error("Échec d'initialisation du moteur", e);
            System.exit(1); // Arrêt propre au lieu de crash
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

        if (!StateValidator.validatePlayerState(currentState)) {
            GameLogger.error("État du joueur invalide, reset...", null);
            // Réinitialiser l'état au lieu de crasher
            resetPlayerState();
            return;
        }

        PlayerState physicsState = physics.update(currentState, deltaTime, grounds);

        if (!StateValidator.validatePlayerState(physicsState)) {
            GameLogger.error("État physique invalide, garde l'ancien état", null);
            return; // Garde l'ancien état
        }

        ThreadManager.playerState.set(physicsState);
    }

    private void resetPlayerState() {
        PlayerState safeState = new PlayerState(
                new Vector2f(0, 0), new Vector2f(0, 0), false,
                AnimationState.IDLE, true, false, false, false,
                5.0f, 10.0f, System.currentTimeMillis()
        );
        ThreadManager.playerState.set(safeState);
        GameLogger.info("PlayerState réinitialisé");
    }

    public void cleanup() {
        Renderer.cleanUp();
        window.cleanup();
        glfwTerminate();
    }
}