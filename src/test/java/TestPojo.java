import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TestPojo {

    private int age;
    private boolean flag;
    private Integer amount;
    private String country;
}
