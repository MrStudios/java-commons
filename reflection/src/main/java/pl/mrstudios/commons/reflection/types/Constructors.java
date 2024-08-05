package pl.mrstudios.commons.reflection.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

public class Constructors {

    public static @NotNull <CLASS> CLASS supplyConstructor(
            @NotNull Constructor<CLASS> constructor,
            @Nullable Object[] parameters
    ) {
        try {
            constructor.setAccessible(true);
            return constructor.newInstance(parameters);
        } catch (@NotNull Exception exception) {
            throw new RuntimeException("Unable to supply constructor.", exception);
        }
    }

}
