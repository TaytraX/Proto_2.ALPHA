package engine;

import engine.world.*;
import entity.PlayerState;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static engine.Engine.worldChunks;

public class ThreadManager {

    public static final AtomicReference<PlayerState> playerState  = new AtomicReference<>();

    public static final BlockingDeque<GroundGenRequest> platformGenQueue = new LinkedBlockingDeque<>();

    public static ExecutorService platformGenerationExecutor;

    public static void initializer() {
        platformGenerationExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r, "GroundGenThread");
            thread.setDaemon(true);
            return thread;
        });

        platformGenerationExecutor.submit(() -> {
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    GroundGenRequest request = platformGenQueue.take();

                    GeneratedGround chunk = new GeneratedGround(request.seed(), request.chunkX());
                    worldChunks.put(request.chunkX(), chunk.getPlatforms());
                    GameLogger.debug("Chunk " + request.chunkX() + " chargé");
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    public static void shutdown() {
        if (platformGenerationExecutor != null) {
            platformGenerationExecutor.shutdown();
        }
    }

}