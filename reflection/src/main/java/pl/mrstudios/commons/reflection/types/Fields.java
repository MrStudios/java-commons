package pl.mrstudios.commons.reflection.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

import static java.lang.String.format;
import static java.util.Arrays.stream;

public class Fields {

    public static void writeField(
            @NotNull String name,
            @NotNull Object instance,
            @Nullable Object value
    ) {
        writeField(field(name, instance.getClass()), instance, value);
    }

    public static void writeField(
            @NotNull Field field,
            @NotNull Object instance,
            @Nullable Object value
    ) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (@NotNull Exception exception) {
            throw new RuntimeException(format("Unable to write value to '%s' field.", field.getName()), exception);
        }
    }

    public static <RETURN> RETURN readField(
            @NotNull String name,
            @NotNull Object instance
    ) {
        return readField(field(name, instance.getClass()), instance);
    }

    public static <RETURN> RETURN readField(
            @NotNull String name,
            @NotNull Class<?> holder
    ) {
        return readField(field(name, holder), null);
    }

    @SuppressWarnings("unchecked")
    public static <RETURN> RETURN readField(
            @NotNull Field field,
            @Nullable Object instance
    ) {
        try {
            field.setAccessible(true);
            return (RETURN) field.get(instance);
        } catch (@NotNull Exception exception) {
            throw new RuntimeException(format("Unable to read value from '%s' field.", field.getName()), exception);
        }
    }

    public static @NotNull Field field(
            @NotNull String name,
            @NotNull Class<?> clazz
    ) {
        return stream(clazz.getDeclaredFields())
                .filter((field) -> field.getName().equals(name))
                .peek((field) -> field.setAccessible(true))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(format("Unable to find '%s' field in '%s' class.", name, clazz.getName())));
    }

}
