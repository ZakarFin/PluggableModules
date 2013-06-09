package fi.zakar.control;

import fi.zakar.annotation.Route;

/**
 * Handler is a common interface for handling requests. Concrete subclasses can be annotated with
 * @Route("handlerKey") to register them for using.
 */
public abstract class Handler {

    /**
     * Returns @Route annotation value if any or defaults to class name
     * @return name of the handler
     */
    public String getName () {
        if(getClass().isAnnotationPresent(Route.class)) {
            Route r = getClass().getAnnotation(Route.class);
            if(!r.value().isEmpty()) {
                return r.value();
            }
        }
        return getClass().getSimpleName();
    }

    /**
     * Hook for setting up components that the handler needs to handle requests
     */
    public void init() {
        // setup services so we can handle requests
    }
    /**
     * Hook for tearing down/destroying components that the handler needed to handle requests
     */
    public void teardown() {
        // clean up as we are about to be destroyed
    }

    /**
     * This is the actual beef of the interface where you should handle the request
     * @param params
     */
    public abstract boolean handleRequest(HandlerParams params) throws HandlerException;
}
