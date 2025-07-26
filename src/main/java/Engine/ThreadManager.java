package Engine;

import Engine.World.*;
import Entity.PlayerState;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadManager {
    // États partagés thread-safe
    public static final AtomicReference<PlayerState> playerState = new AtomicReference<>();

    // Communication entre threads
    private static final BlockingQueue<GroundGenRequest> chunkRequests = new LinkedBlockingQueue<>();
    private static final BlockingQueue<GeneratedGround> completedChunks = new LinkedBlockingQueue<>();

    // Thread pool configuré
    private static final ExecutorService worldGenExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "WorldGen-Thread");
        t.setDaemon(true); // Meurt avec le programme
        return t;
    });

    public static void requestChunk(int chunkX, long seed) {
        chunkRequests.offer(new GroundGenRequest(chunkX, seed));
    }

    public static GeneratedGround pollCompletedChunk() {
        return completedChunks.poll(); // Non-blocking
    }

    // Démarrer le worker thread
    public static void startWorldGeneration() {
        worldGenExecutor.submit(new GroundGenRequest());
    }
}