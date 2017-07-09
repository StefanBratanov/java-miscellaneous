package lib;

public class Runnables {

    private Runnables() {
    }

    public static void tryRun(ThrowableRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable ex) {
            throw new IllegalStateException(ex);
        }
    }

    @FunctionalInterface
    public interface ThrowableRunnable {

        void run() throws Throwable;
    }
}
