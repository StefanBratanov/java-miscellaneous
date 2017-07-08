import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NullValidator {

    private static final Pattern GETTER_PREFIX_PATTERN = Pattern.compile("^(get|is)(?<fieldName>.*)");

    private NullValidator() {
    }

    public static <T> void validate(T t) {

        List<String> nullFields = Arrays.stream(t.getClass().getDeclaredMethods())
                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                .filter(m -> GETTER_PREFIX_PATTERN.matcher(m.getName()).matches())
                .filter(m -> {
                    Object value = Suppliers.tryAndGet(() -> m.invoke(t));
                    return Objects.isNull(value);
                })
                .map(m -> {
                    Matcher matcher = GETTER_PREFIX_PATTERN.matcher(m.getName());
                    matcher.find();
                    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, matcher.group("fieldName"));
                })
                .collect(Collectors.toList());

        if (!nullFields.isEmpty()) {
            String exceptionMessage = String.format("[%s] is/are null for %s", Joiner.on(",").join(nullFields), t.getClass().getSimpleName());
            throw new IllegalStateException(exceptionMessage);
        }

    }
}
