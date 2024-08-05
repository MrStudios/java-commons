package pl.mrstudios.commons.reflection.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.Arrays.stream;

public class Methods {

    public static @NotNull <RETURN> RETURN supplyMethod(
            @NotNull String name,
            @NotNull Object instance,
            @Nullable Object[] parameters
    ) {
        return supplyMethod(
                method(
                        name, instance.getClass(),
                        stream(parameters).filter(Objects::nonNull)
                        .map(Object::getClass).toArray(Class[]::new)
                ), instance, parameters
        );
    }

    @SuppressWarnings("unchecked")
    public static @NotNull <RETURN> RETURN supplyMethod(
            @NotNull Method method,
            @NotNull Object instance,
            @Nullable Object[] parameters
    ) {
        try {
            method.setAccessible(true);
            return (RETURN) method.invoke(instance, parameters);
        } catch (@NotNull Exception exception) {
            throw new RuntimeException(format("Unable to supply '%s' method.", method.getName()), exception);
        }
    }

    public static @NotNull Method method(
            @NotNull String name,
            @NotNull Class<?> clazz,
            @NotNull Class<?>... parameters
    ) {
        try {
            return clazz.getDeclaredMethod(name, parameters);
        } catch (@NotNull Exception exception) {
            throw new RuntimeException(format("Unable to find '%s' method.", name), exception);
        }
    }

}
