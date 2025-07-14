package Engine;

import Entity.AnimationState;
import Entity.Camera;
import Entity.Player;
import Entity.PlayerState;
import Laucher.Main;
import Render.Window;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private float deltaTime = 0.000016f;

    List<AABB> horizontalPlatforms = new ArrayList<>(); // Pour les collisions Y
    List<AABB> verticalWalls = new ArrayList<>();
    Map<Integer, List<AABB>> grounds = new HashMap<>();

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
                    false,
                    0.0f,
                    AnimationState.IDLE,
                    true,
                    false,
                    false,
                    false,
                    20.0f,
                    2.0f,
                    480.0f,
                    System.currentTimeMillis()
            );
            ThreadManager.playerState.set(initialState);
            GameLogger.info("PlayerState initialisé");

            // Plateforme de base
            horizontalPlatforms.add(new AABB(new Vector2f(0, -10), new Vector2f(49, 0.5f)));
            // Plateforme supplémentaire
            horizontalPlatforms.add(new AABB(new Vector2f(3, 0), new Vector2f(2, 0.5f)));

            // Vérification que l'état est bien défini
            PlayerState test = ThreadManager.playerState.get();
            if (test == null) {
                GameLogger.error("PlayerState est null après initialisation !", null);
                throw new Exception("PlayerState initialization failed");
            }

            window.init();
            Renderer = new Renderer();
            Renderer.initialize();
            camera = new Camera();
            player = new Player();
            physics = new Physics();

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

                update();
                render();
            }
        cleanup();
    }

    public void update() {
        // 1. Récupérer l'état actuel
        PlayerState currentState = ThreadManager.playerState.get();

        // 2. Traiter les inputs et mouvements
        PlayerState afterInputs  = player.update(currentState, deltaTime);
        // 2. Physics applique velocity à position
        PlayerState afterPhysics = physics.update(afterInputs,horizontalPlatforms, verticalWalls, deltaTime);

        // 4. Sauvegarder le nouvel état
        ThreadManager.playerState.set(afterPhysics);
    }

    private void resetPlayerState() {
    }

    public void cleanup() {
        Renderer.cleanUp();
        window.cleanup();
        glfwTerminate();
    }
}