public class Suppliers {

    public static <T> T tryAndGet(ThrowableSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    @FunctionalInterface
    public interface ThrowableSupplier<T> {

        T get() throws Exception;
    }
}
