package pl.mrstudios.commons.reflection.types;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.lang.Class.forName;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static pl.mrstudios.commons.reflection.types.Packages.packagesInPackage;
import static pl.mrstudios.commons.reflection.types.Packages.resolvePackage;

public class Classes {

    @SuppressWarnings("rawtypes")
    public static List<Class> classesInPackage(
            @NotNull String packageName
    ) {
        return packagesInPackage(packageName).stream()
                .flatMap(
                        (pkg) -> stream(requireNonNull(resolvePackage(pkg).list((directory, name) -> name.endsWith(".class"))))
                                .filter((name) -> !name.contains("$"))
                                .map((name) -> name.replace(".class", ""))
                                .map((name) -> format("%s.%s", pkg, name))
                ).map((name) -> (Class) classForName(name))
                .toList();
    }

    public static Class<?> classForName(
            @NotNull String name
    ) {
        try {
            return forName(name);
        } catch (@NotNull Exception exception) {
            throw new RuntimeException(format("Unable to find '%s' class.", name), exception);
        }
    }

}
