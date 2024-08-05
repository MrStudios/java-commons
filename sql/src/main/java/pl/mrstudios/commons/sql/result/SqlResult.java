package pl.mrstudios.commons.sql.result;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.List.copyOf;

public class SqlResult implements Collection<SqlEntry> {

    private final Collection<SqlEntry> entries; {
        this.entries = new LinkedList<>();
    }

    public @NotNull SqlEntry entry(
            @NotNull String key
    ) {
        return this.entries.stream()
                .filter((entry) -> entry.key().equalsIgnoreCase(key))
                .findFirst().orElseThrow();
    }

    public @NotNull Collection<SqlEntry> entries() {
        return copyOf(this.entries);
    }

    @Override
    public int size() {
        return this.entries.size();
    }

    @Override
    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    @Override
    public boolean contains(
            @NotNull Object object
    ) {
        return this.entries.contains(object);
    }

    @Override
    public @NotNull Iterator<SqlEntry> iterator() {
        return this.entries.iterator();
    }

    @Override
    public void forEach(
            @NotNull Consumer<? super SqlEntry> action
    ) {
        this.entries.forEach(action);
    }

    @Override
    public @NotNull Object[] toArray() {
        return this.entries.toArray();
    }

    @Override
    public @NotNull <T> T[] toArray(
            @NotNull T[] array
    ) {
        return this.entries.toArray(array);
    }

    @Override
    public boolean add(
            @NotNull SqlEntry sqlEntry
    ) {
        return this.entries.add(sqlEntry);
    }

    @Override
    public boolean remove(
            @NotNull Object object
    ) {
        return this.entries.remove(object);
    }

    @Override
    public boolean containsAll(
            @NotNull Collection<?> collection
    ) {
        return this.entries.containsAll(collection);
    }

    @Override
    public boolean addAll(
            @NotNull Collection<? extends SqlEntry> collection
    ) {
        return this.entries.addAll(collection);
    }

    @Override
    public boolean removeAll(
            @NotNull Collection<?> collection
    ) {
        return this.entries.removeAll(collection);
    }

    @Override
    public boolean retainAll(
            @NotNull Collection<?> collection
    ) {
        return this.entries.retainAll(collection);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Unable to clear entities due to limited access.");
    }

    @Override
    public @NotNull Stream<SqlEntry> stream() {
        return this.entries.stream();
    }

    @Override
    public @NotNull Stream<SqlEntry> parallelStream() {
        return this.entries.parallelStream();
    }

}
