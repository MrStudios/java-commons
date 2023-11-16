package pl.mrstudios.commons.inject.exception;

public class InjectConstructorException extends RuntimeException {

    public InjectConstructorException(String message) {
        super(message);
    }

    public InjectConstructorException(String message, Throwable cause) {
        super(message, cause);
    }

}
