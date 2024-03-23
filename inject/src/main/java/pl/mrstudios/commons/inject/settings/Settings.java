package pl.mrstudios.commons.inject.settings;

import org.jetbrains.annotations.NotNull;

public class Settings {

    private boolean ignoreMissingAnnotation = false;

    public @NotNull Settings ignoreMissingAnnotation(boolean value) {
        this.ignoreMissingAnnotation = value;
        return this;
    }

    public boolean ignoreMissingAnnotation() {
        return this.ignoreMissingAnnotation;
    }

}
