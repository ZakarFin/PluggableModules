package fi.zakar.control;

/**
 * Exception that fi.zakar.control.Handlers throw if they are unable to handle the request.
 */
public class HandlerException extends RouterException {

    public HandlerException(final String msg) {
        super(msg);
    }

    public HandlerException(final String msg, Exception cause) {
        super(msg, cause);
    }
}
