package lib;

import com.google.common.base.CaseFormat;
import com.google.common.base.Joiner;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static java.lang.invoke.MethodType.methodType;
import static java.util.Arrays.stream;

public class NullValidator {

    private static final Pattern GETTER_PREFIX_PATTERN = Pattern.compile("^(get|is)(?<fieldName>.*)");

    private NullValidator() {
    }

    public static <T> void validate(T t) {

        List<String> nullFields = stream(t.getClass().getDeclaredMethods())
                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                .filter(m -> GETTER_PREFIX_PATTERN.matcher(m.getName()).matches())
                .filter(m -> {
                    Object value = Suppliers.tryGet(() -> m.invoke(t));
                    return Objects.isNull(value);
                })
                .map(m -> {
                    Matcher matcher = GETTER_PREFIX_PATTERN.matcher(m.getName());
                    matcher.find();
                    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, matcher.group("fieldName"));
                })
                .collect(Collectors.toList());

        throwExceptionWhenThereAreNullFieldsFor(t, nullFields);

    }

    public static <T> void validateUsingMethodHandles(T t) {

        List<String> nullFields = stream(t.getClass().getDeclaredFields())
                .filter(f -> {
                    MethodHandles.Lookup lookup = MethodHandles.lookup();
                    Class<?> fieldType = f.getType();
                    String getterPrefix = fieldType.equals(boolean.class) ? "is" : "get";
                    String capitalizedFieldName = LOWER_CAMEL.to(UPPER_CAMEL, f.getName());
                    String getterName = getterPrefix + capitalizedFieldName;
                    MethodHandle getterHandle =
                            Suppliers.tryGet(() -> lookup.findVirtual(t.getClass(), getterName,
                                    methodType(fieldType)));
                    Object value = Suppliers.tryGet(() -> getterHandle.invoke(t));
                    return Objects.isNull(value);
                })
                .map(Field::getName)
                .collect(Collectors.toList());

        throwExceptionWhenThereAreNullFieldsFor(t, nullFields);
    }

    private static <T> void throwExceptionWhenThereAreNullFieldsFor(T t, List<String> nullFields) {
        if (!nullFields.isEmpty()) {
            String exceptionMessage = String.format("[%s] is/are null for %s", Joiner.on(",").join(nullFields), t.getClass().getSimpleName());
            throw new IllegalStateException(exceptionMessage);
        }
    }

}
