package Engine;

import Engine.World.*;
import Entity.PlayerState;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class ThreadManager {

    public static final AtomicReference<PlayerState> playerState  = new AtomicReference<>();

    public static final BlockingDeque<GroundGenRequest> platformGenQueue = new LinkedBlockingDeque<>();
    public static final BlockingDeque<GeneratedGround> GENERATED_PLATFORMS = new LinkedBlockingDeque<>();

    public ExecutorService platformGenerationExecutor;

    public ThreadManager() {

        platformGenerationExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            return thread;
        });
    }
}