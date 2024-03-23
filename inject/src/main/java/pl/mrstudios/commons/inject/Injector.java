package pl.mrstudios.commons.inject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.mrstudios.commons.inject.annotation.Inject;
import pl.mrstudios.commons.inject.exception.InjectConstructorException;
import pl.mrstudios.commons.inject.settings.Settings;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static java.util.Arrays.stream;

public class Injector {

    private final Settings settings;
    private final Map<Class<?>, Object> services;

    public Injector() {
        this.settings = new Settings();
        this.services = new HashMap<>();
    }

    public Injector(
            @NotNull Settings settings
    ) {
        this.settings = settings;
        this.services = new HashMap<>();
    }

    public Injector(
            @NotNull Consumer<Settings> consumer
    ) {
        this();
        consumer.accept(this.settings);
    }

    @SuppressWarnings("unchecked")
    public @Nullable <CLASS> CLASS inject(
            @NotNull Class<CLASS> clazz
    ) {

        AtomicReference<CLASS> type = new AtomicReference<>(null);

        if (stream(clazz.getDeclaredConstructors()).noneMatch((constructor) -> constructor.isAnnotationPresent(Inject.class)))
            if (!this.settings.ignoreMissingAnnotation())
                throw new InjectConstructorException("Could not find any constructor annotated with @Inject in class " + clazz.getName() + ".");
            else
                return null;

        stream(clazz.getDeclaredConstructors())
                .forEach((constructor) -> {

                    try {

                        if (type.get() != null)
                            return;

                        Object[] constructorArguments = stream(constructor.getParameterTypes())
                                .map(this.services::get)
                                .toArray();

                        type.set((CLASS) constructor.newInstance(constructorArguments));

                    } catch (@NotNull Exception exception) {
                        throw new InjectConstructorException("Could not inject arguments into class " + clazz.getName() + " constructor.", exception);
                    }

                });

        return type.get();

    }

    public @NotNull Injector register(
            @NotNull Object service
    ) {
        this.services.put(service.getClass(), service);
        return this;
    }

    public @NotNull Injector register(
            @NotNull Class<?> clazz,
            @NotNull Object service
    ) {
        this.services.put(clazz, service);
        return this;
    }

}
