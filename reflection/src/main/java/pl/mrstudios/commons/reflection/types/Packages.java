package pl.mrstudios.commons.reflection.types;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;

import static java.lang.String.format;
import static java.lang.Thread.currentThread;
import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

public class Packages {

    public static @NotNull File resolvePackage(
            @NotNull String packageName
    ) {
        return new File(decode(
                requireNonNull(classLoader.getResource(packageName.replace('.', '/'))).getFile(),
                UTF_8
        ));
    }

    public static @NotNull Collection<String> packagesInPackage(
            @NotNull String packageName
    ) {
        return stream(requireNonNull(resolvePackage(packageName).listFiles(File::isDirectory)))
                .map((file) -> format("%s.%s", packageName, file.getName()))
                .flatMap((name) -> concat(of(name), packagesInPackage(name).stream()))
                .toList();
    }

    protected static final ClassLoader classLoader = currentThread().getContextClassLoader();

}
