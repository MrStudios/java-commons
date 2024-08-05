package pl.mrstudios.commons.inject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.mrstudios.commons.inject.annotation.Inject;
import pl.mrstudios.commons.inject.exception.InjectConstructorException;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static pl.mrstudios.commons.reflection.types.Constructors.supplyConstructor;

public class Injector {

    private final Map<Class<?>, Object> services;

    public Injector() {
        this.services = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public @Nullable <CLASS> CLASS inject(
            @NotNull Class<CLASS> clazz
    ) {
        return (CLASS) stream(clazz.getDeclaredConstructors())
                .filter((constructor) -> constructor.isAnnotationPresent(Inject.class))
                .map((constructor) -> supplyConstructor(
                        constructor, stream(constructor.getParameterTypes())
                                .map(this.services::get)
                                .toArray()
                )).findFirst()
                .orElseThrow(() -> new InjectConstructorException(format("Unable to inject parameters into constructor of '%s' class.", clazz.getName())));
    }

    public @NotNull Injector registerService(
            @NotNull Object service
    ) {
        this.services.put(service.getClass(), service);
        return this;
    }

    public @NotNull Injector registerService(
            @NotNull Class<?> clazz,
            @NotNull Object service
    ) {
        this.services.put(clazz, service);
        return this;
    }

    public static @NotNull Injector injector() {
        return new Injector();
    }

}
