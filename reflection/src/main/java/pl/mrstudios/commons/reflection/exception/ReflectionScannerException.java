package pl.mrstudios.commons.reflection.exception;

import org.jetbrains.annotations.NotNull;

public class ReflectionScannerException extends RuntimeException {

    public ReflectionScannerException(
            @NotNull String message
    ) {
        super(message);
    }

    public ReflectionScannerException(
            @NotNull String message,
            @NotNull Throwable cause
    ) {
        super(message, cause);
    }

}
