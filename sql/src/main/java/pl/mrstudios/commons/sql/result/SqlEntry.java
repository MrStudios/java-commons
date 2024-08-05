package pl.mrstudios.commons.sql.result;

import org.jetbrains.annotations.NotNull;

public record SqlEntry(
        @NotNull String key,
        @NotNull Class<?> type,
        @NotNull Object object
) {

    public @NotNull Long asLong() {
        return (Long) this.object;
    }

    public @NotNull String asString() {
        return (String) this.object;
    }

    public @NotNull Integer asInteger() {
        return (Integer) this.object;
    }

    public @NotNull Double asDouble() {
        return (Double) this.object;
    }

    public @NotNull Object asObject() {
        return this.object;
    }

}
