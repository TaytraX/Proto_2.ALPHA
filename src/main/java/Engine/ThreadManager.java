package Engine;

import Engine.World.*;
import Entity.PlayerState;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadManager {

    public static final AtomicReference<PlayerState> playerState  = new AtomicReference<>();

    public static final BlockingDeque<PlatformGenRequest> platformGenQueue = new LinkedBlockingQueue<>();
    public static final BlockingDeque<GeneratedPlatforms> generatedPlatforms = new LinkedBlockingQueue<>();


    public ExecutorService platformGenerationExecutor;
    public ExecutorService gameplayExecutor;

    public ThreadManager() {

        platformGenerationExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            return thread;
        });

        gameplayExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
    }

}