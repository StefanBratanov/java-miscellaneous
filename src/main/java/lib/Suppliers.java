package lib;

public class Suppliers {

    private Suppliers() {}

    public static <T> T tryGet(ThrowableSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Throwable ex) {
            throw new IllegalStateException(ex);
        }
    }

    @FunctionalInterface
    public interface ThrowableSupplier<T> {

        T get() throws Throwable;
    }
}
