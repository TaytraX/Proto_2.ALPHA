package Engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadManager {

    public static ReentrantLock playerlock = new ReentrantLock();
    public static ReentrantLock logiclock = new ReentrantLock();

    public static ExecutorService worldGenerationExecutor;
    public static ExecutorService gameplayExecutor;

    public ThreadManager() {

        worldGenerationExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });

        gameplayExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        });
    }

}