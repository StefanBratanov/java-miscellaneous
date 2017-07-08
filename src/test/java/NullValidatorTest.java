import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class NullValidatorTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void doesNotFail() {
        TestPojo testPojo = TestPojo.builder()
                .age(10)
                .amount(1)
                .country("Bulgaria")
                .flag(true)
                .build();

        NullValidator.validate(testPojo);

    }

    @Test
    public void failsIfSomeFieldsAreNull() {
        TestPojo testPojo = TestPojo.builder()
                .age(10).build();

        exception.expect(IllegalStateException.class);
        exception.expectMessage("[country,amount] is/are null for TestPojo");

        NullValidator.validate(testPojo);
    }

}