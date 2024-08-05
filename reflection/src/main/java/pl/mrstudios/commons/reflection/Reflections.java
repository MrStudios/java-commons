package pl.mrstudios.commons.reflection;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;

import static java.util.Arrays.asList;
import static pl.mrstudios.commons.reflection.types.Classes.classesInPackage;

@SuppressWarnings("rawtypes")
public class Reflections<CLASS> implements Iterable<Class> {

    private final Collection<Class> collection;

    public Reflections(
            @NotNull String packageName
    ) {
        this.collection = classesInPackage(
                (packageName.lastIndexOf('.') == -1) ?
                        packageName : packageName.substring(0, packageName.lastIndexOf('.'))
        );
    }

    @SuppressWarnings("unchecked")
    public @NotNull Collection<Class<CLASS>> getClassesImplementing(
            @NotNull Class<?> interfaceClass
    ) {
        return this.collection.stream()
                .filter((clazz) -> asList(clazz.getInterfaces()).contains(interfaceClass))
                .map((clazz) -> (Class<CLASS>) clazz)
                .toList();
    }

    @SuppressWarnings("unchecked")
    public @NotNull Collection<Class<CLASS>> getClassesAnnotatedWith(
            @NotNull Class<? extends Annotation> annotationClass
    ) {
        return this.collection.stream()
                .filter((clazz) -> clazz.isAnnotationPresent(annotationClass))
                .map((clazz) -> (Class<CLASS>) clazz)
                .toList();
    }

    @Override
    public @NotNull Iterator<Class> iterator() {
        return this.collection.iterator();
    }

    public static @NotNull Reflections<?> reflections(
            @NotNull String packageName
    ) {
        return new Reflections<>(packageName);
    }

    public static @NotNull <TYPE> Reflections<TYPE> reflections(
            @NotNull String packageName,
            @NotNull Class<TYPE> typeClass
    ) {
        return new Reflections<>(packageName);
    }

}
