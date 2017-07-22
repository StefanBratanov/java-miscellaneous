package serialization;

import com.google.common.collect.Streams;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import lib.TestPojo;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class SerializationIT {

    private FileSystem fileSystem;

    @Before
    public void init() {
        fileSystem = Jimfs.newFileSystem(Configuration.unix());
    }

    @Test
    public void testSerializingAndDeserializing() {

        TestPojo testPojo = testPojo();
        TestPojo testPojo1 = testPojo1();

        Path testPath = fileSystem.getPath("test.txt");

        Stream.of(testPojo,testPojo1)
                .collect(SerializingFileCollector.create(testPath));

        List<TestPojo> result = Streams.stream(DeserializingFileIterator.create(testPath, TestPojo.class))
                .collect(Collectors.toList());

        assertThat(result).containsExactly(testPojo,testPojo1);

    }

    private TestPojo testPojo() {
        return TestPojo.builder()
                .age(12)
                .amount(13)
                .flag(true)
                .country("Bulgaria")
                .build();
    }

    private TestPojo testPojo1() {
        return TestPojo.builder()
                .age(69)
                .amount(7000)
                .flag(true)
                .country("United Kingdom")
                .build();
    }

}