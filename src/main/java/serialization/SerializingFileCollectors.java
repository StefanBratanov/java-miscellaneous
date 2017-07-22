package serialization;

import com.esotericsoftware.kryo.Kryo;

import java.nio.file.Path;
import java.util.Spliterators;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SerializingFileCollectors {

    public static <T> Collector<T, Kryo, Void> create(Path path) {
        return SerializingFileCollector.create(path);
    }

    public static <T> Stream<T> read(Path path, Class<T> model) {
        DeserializingFileIterator<T> iterator = DeserializingFileIterator.create(path, model);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }
}
