package serialization;

import com.google.common.base.Stopwatch;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import lib.TestPojo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class SerializationIT {

    private FileSystem fileSystem;

    @Before
    public void init() {
        fileSystem = Jimfs.newFileSystem(Configuration.unix());
    }

    @After
    public void after() throws IOException {
        fileSystem.close();
    }

    @Test
    public void testSerializingAndDeserializing() {

        TestPojo testPojo = testPojo();
        TestPojo testPojo1 = testPojo1();

        Path testPath = fileSystem.getPath("test.txt");

        Stream.of(testPojo, testPojo1)
                .collect(SerializingFileCollectors.create(testPath));

        List<TestPojo> result = SerializingFileCollectors.read(testPath, TestPojo.class)
                .collect(Collectors.toList());

        assertThat(result).containsExactly(testPojo, testPojo1);
    }

    @Test
    public void testPerformance() {

        Path testPath = fileSystem.getPath("test.txt");

        Long size = 1000000L;

        Stopwatch stopwatch = Stopwatch.createStarted();

        IntStream.iterate(0, i -> i + 1)
                .limit(size)
                .mapToObj(i -> testPojo())
                .collect(SerializingFileCollectors.create(testPath));

        log.info("Elapsed time in ms(writing): " + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        stopwatch.reset();
        stopwatch.start();

        List<TestPojo> result = SerializingFileCollectors.read(testPath, TestPojo.class)
                .collect(Collectors.toList());

        log.info("Elapsed time in ms(reading): " + stopwatch.elapsed(TimeUnit.MILLISECONDS));

        assertThat(result).hasSize(size.intValue());

        //a random element from the list is equal to testPojo()
        assertThat(result.get((int) (Math.random() * size) - 1)).isEqualTo(testPojo());

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