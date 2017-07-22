package serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import lib.Suppliers;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.nio.file.Files.newOutputStream;

public class SerializingFileCollector<T> implements Collector<T, Kryo, Void> {

    private volatile Output output;
    private volatile Kryo kryo;

    private SerializingFileCollector(Path path) {
        OutputStream outputStream = Suppliers.tryGet(() -> newOutputStream(path));
        output = new Output(outputStream);
    }

    @Override
    public Supplier<Kryo> supplier() {
        kryo = new Kryo();
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        return () -> kryo;
    }

    @Override
    public BiConsumer<Kryo, T> accumulator() {
        return ((kryo, o) -> kryo.writeObject(output,o));
    }

    @Override
    public BinaryOperator<Kryo> combiner() {
        return (i1,i2) -> i1;
    }

    @Override
    public Function<Kryo, Void> finisher() {
        return (kryo) -> {
            output.close();
            return null;
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }

    public static <T> SerializingFileCollector<T> create(Path path) {
        return new SerializingFileCollector<>(path);
    }
}
