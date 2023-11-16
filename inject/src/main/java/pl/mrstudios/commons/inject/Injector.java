package pl.mrstudios.commons.inject;

import org.jetbrains.annotations.Nullable;
import pl.mrstudios.commons.inject.annotation.Inject;
import pl.mrstudios.commons.inject.exception.InjectConstructorException;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Injector {

    private final Map<Class<?>, Object> services;

    public Injector() {
        this.services = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public @Nullable <T> T inject(Class<T> clazz) {

        AtomicReference<T> type = new AtomicReference<>(null);

        if (Arrays.stream(clazz.getDeclaredConstructors()).noneMatch((constructor) -> constructor.isAnnotationPresent(Inject.class)))
            throw new InjectConstructorException("Could not find any constructor annotated with @Inject in class " + clazz.getName() + ".");

        Arrays.stream(clazz.getDeclaredConstructors())
                .forEach((constructor) -> {

                    try {

                        if (type.get() != null)
                            return;

                        Object[] constructorArguments = Arrays.stream(constructor.getParameterTypes())
                                .map(this.services::get)
                                .toArray();

                        type.set((T) constructor.newInstance(constructorArguments));

                    } catch (Exception exception) {
                        throw new InjectConstructorException("Could not inject arguments into class " + clazz.getName() + " constructor.", exception);
                    }

                });

        return type.get();

    }

    public Injector register(Object service) {
        this.services.put(service.getClass(), service);
        return this;
    }

}
