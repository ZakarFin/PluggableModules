package fi.zakar.control;

/**
 * Exception that fi.zakar.control.Router throws if something unexpected goes wrong when routing.
 */
public class RouterException extends Exception {

    public RouterException(final String msg) {
        super(msg);
    }

    public RouterException(final String msg, Exception cause) {
        super(msg, cause);
    }
}
