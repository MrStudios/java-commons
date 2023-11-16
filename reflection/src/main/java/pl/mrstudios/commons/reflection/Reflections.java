package pl.mrstudios.commons.reflection;

import pl.mrstudios.commons.reflection.exception.ReflectionScannerException;

import java.io.File;
import java.lang.annotation.Annotation;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public record Reflections<T>(String packageName) {

    @SuppressWarnings("unchecked")
    public Collection<Class<? extends T>> getClassesImplementing(Class<?> iClass) {

        List<Class<? extends T>> collection = new ArrayList<>();

        this.getProcessEntries().forEach((entry) -> {

            if (!entry.getName().endsWith(".class") || entry.getName().contains("$"))
                return;

            if (!entry.getName().startsWith(this.packageName.replace('.', '/')))
                return;

            try {

                Class<?> clazz = Class.forName(entry.getName().replace('/', '.').replace(".class", ""));
                for (Class<?> iFace : clazz.getInterfaces())
                    if (iFace.equals(iClass))
                        collection.add((Class<? extends T>) clazz);

            } catch (ClassNotFoundException | NoClassDefFoundError ignored) {}

        });

        return collection;

    }

    @SuppressWarnings("unchecked")
    public Collection<Class<? extends T>> getClassesAnnotatedWith(Class<? extends Annotation> aClass) {

        List<Class<? extends T>> collection = new ArrayList<>();

        this.getProcessEntries().forEach((entry) -> {

            if (!entry.getName().endsWith(".class") || entry.getName().contains("$"))
                return;

            if (!entry.getName().startsWith(this.packageName.replace('.', '/')))
                return;

            try {

                Class<?> clazz = Class.forName(entry.getName().replace('/', '.').replace(".class", ""));
                if (clazz.isAnnotationPresent(aClass))
                    collection.add((Class<? extends T>) clazz);

            } catch (ClassNotFoundException | NoClassDefFoundError ignored) {}

        });

        return collection;

    }

    private Collection<JarEntry> getProcessEntries() {

        List<JarEntry> collection = new ArrayList<>();
        CodeSource codeSource = this.getClass().getProtectionDomain().getCodeSource();

        if (codeSource == null)
            throw new ReflectionScannerException("Unable to fetch process classes because CodeSource is null..");

        try (JarFile jarFile = new JarFile(new File(codeSource.getLocation().toURI()))) {

            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements())
                collection.add(entries.nextElement());

        } catch (Exception exception) {
            throw new ReflectionScannerException("Unable to fetch process classes because jar file does not exists or is not accessible.", exception);
        }

        return collection;

    }

}
