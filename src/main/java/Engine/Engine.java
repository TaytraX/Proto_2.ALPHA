package Engine;

import Engine.World.GeneratedGround;
import Entity.AnimationState;
import Entity.Camera;
import Entity.Player;
import Entity.PlayerState;
import Laucher.Main;
import Render.DisplayManager;
import Render.Window;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static Engine.World.GeneratedGround.CHUNK_WIDTH;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class Engine {
    public Window window;
    public boolean isRunning;
    public Renderer Renderer;
    public Camera camera;
    public Player player;
    private Physics physics;
    private long lastTime = System.currentTimeMillis();
    private float deltaTime = 0.000016f;
    private Map<Integer, List<AABB>> worldChunks = new ConcurrentHashMap<>();
    Random random = new Random();
    private final long seed = random.nextLong();

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
                    new Vector2f(0, 30),
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
                    12.0f,
                    System.currentTimeMillis()
            );
            ThreadManager.playerState.set(initialState);
            GameLogger.info("PlayerState initialisé");

            generateInitialWorld();

            // Vérification que l'état est bien défini
            PlayerState test = ThreadManager.playerState.get();
            if (test == null) {
                GameLogger.error("PlayerState est null après initialisation !", null);
                throw new Exception("PlayerState initialization failed");
            }

            window.init();
            DisplayManager display = window.getDisplayManager();
            display.setScaleMode(DisplayManager.ScaleMode.LETTERBOX); // ou autre mode

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

    private void generateInitialWorld() {
        // Générer 5 chunks autour du spawn (x=0)
        for (int chunkX = -2; chunkX <= 2; chunkX++) {
            loadChunks(chunkX);
        }
        GameLogger.info("Monde initial généré : " + getTotalBlockCount() + " blocs");
    }

    public void unloadChunk(int chunkX) {
        if (worldChunks.remove(chunkX) != null) {
            GameLogger.debug("Chunk " + chunkX + " déchargé");
        }
    }

    // Méthode pour obtenir toutes les plateformes actives (pour la physique)
    public List<AABB> getActivePlatforms() {
        List<AABB> allPlatforms = new ArrayList<>();
        for (List<AABB> chunkPlatforms : worldChunks.values()) {
            allPlatforms.addAll(chunkPlatforms);
        }
        return allPlatforms;
    }

    // Décharger les chunks lointains pour économiser la mémoire
    public void manageChunks() {
        PlayerState playerState = ThreadManager.playerState.get();
        if (playerState == null) return;

        int playerChunkX = (int)(playerState.position().x / CHUNK_WIDTH);

        // ✅ Utilise unloadChunk() au lieu de removeIf
        List<Integer> chunksToUnload = new ArrayList<>();

        for (Integer chunkX : worldChunks.keySet()) {
            if (Math.abs(chunkX - playerChunkX) > 3) {
                chunksToUnload.add(chunkX);
            }
        }

        for (Integer chunkX : chunksToUnload) {
            unloadChunk(chunkX);
        }
    }

    private int getTotalBlockCount() {
        return worldChunks.values().stream()
                .mapToInt(List::size)
                .sum();
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

        camera.followPlayer(deltaTime);
        // 2. Physics applique velocity à position
        PlayerState afterPhysics = physics.update(afterInputs, getPlatformsNearPlayer(), deltaTime);

        // 4. Sauvegarder le nouvel état
        ThreadManager.playerState.set(afterPhysics);

        manageChunks();
    }

    // Optionnel : méthode pour changer le mode en jeu
    public void toggleScaleMode() {
        DisplayManager display = window.getDisplayManager();
        DisplayManager.ScaleMode current = display.getScaleMode();

        switch (current) {
            case LETTERBOX -> display.setScaleMode(DisplayManager.ScaleMode.STRETCH);
            case STRETCH -> display.setScaleMode(DisplayManager.ScaleMode.INTEGER);
            case INTEGER -> display.setScaleMode(DisplayManager.ScaleMode.LETTERBOX);
        }
    }

    public void loadChunks(int chunkX) {
        if (!worldChunks.containsKey(chunkX)) {
            GeneratedGround chunk = new GeneratedGround(seed, chunkX);
            worldChunks.put(chunkX, chunk.getPlatforms());
            GameLogger.info("Chunk " + chunkX + " chargé");
        }
    }

    public int getChunkX(float worldX) {
        return (int) Math.floor(worldX / CHUNK_WIDTH);
    }

    public List<AABB> getPlatformsNearPlayer() {
        PlayerState state = ThreadManager.playerState.get();

        if (state == null) return new ArrayList<>();

        int playerChunkX = getChunkX(state.position().x);
        List<AABB> nearbyPlatforms = new ArrayList<>();

        for (int chunkX = playerChunkX - 2; chunkX <= playerChunkX + 2; chunkX++) {
            loadChunks(chunkX);

            List<AABB> chunkPlatforms = worldChunks.get(chunkX);
            if (chunkPlatforms != null) {
                nearbyPlatforms.addAll(chunkPlatforms);
            }
        }
        return nearbyPlatforms;
    }

    public void cleanup() {
        Renderer.cleanUp();
        window.cleanup();
        glfwTerminate();
    }
}