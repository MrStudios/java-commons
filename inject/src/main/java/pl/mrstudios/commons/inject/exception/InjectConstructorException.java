package pl.mrstudios.commons.inject.exception;

import org.jetbrains.annotations.NotNull;

public class InjectConstructorException extends RuntimeException {

    public InjectConstructorException(
            @NotNull String message
    ) {
        super(message);
    }

    public InjectConstructorException(
            @NotNull String message,
            @NotNull Throwable cause
    ) {
        super(message, cause);
    }

}
