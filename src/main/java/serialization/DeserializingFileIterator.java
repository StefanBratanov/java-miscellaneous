package serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import lib.Suppliers;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

public class DeserializingFileIterator<T> implements Iterator<T> {

    private volatile Kryo kryo;
    private volatile Input input;
    private Class<T> model;

    private DeserializingFileIterator(Path path, Class<T> model) {
        kryo = new Kryo();
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        InputStream inputStream = Suppliers.tryGet(() -> Files.newInputStream(path));
        input = new Input(inputStream);
        this.model = model;
    }

    @Override
    public boolean hasNext() {
        return Suppliers.tryGet(() -> {
            if (input.available() == 0) {
                input.close();
                return false;
            } else {
                return true;
            }
        });
    }

    @Override
    public T next() {
        return kryo.readObject(input, model);
    }

    public static <T> DeserializingFileIterator<T> create(Path path, Class<T> model) {
        return new DeserializingFileIterator<>(path, model);
    }
}
