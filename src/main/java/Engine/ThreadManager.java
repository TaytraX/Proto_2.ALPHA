package Engine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadManager {

    public static ReentrantLock playerlock = new ReentrantLock();
    public static ReentrantLock logiclock = new ReentrantLock();

    public static ExecutorService logicExecutor;
    public static ExecutorService playerExecutor;
}