package fi.zakar.control;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * A router class that finds fi.zakar.control.Handlers through SPI ServiceLoader and can be used to route
 * requests to these handlers.
 */
public class Router {

    private Map<String, Handler> routes = new HashMap<String, Handler>();

    public Router() {
        loadRoutes();
    }

    /**
     * Load handlers through ServiceLoader. The common way the end up finding through this
     * is the @Route annotation.
     */
    private void loadRoutes() {
        ServiceLoader<Handler> impl = ServiceLoader.load(Handler.class);

        for (Handler loadedImpl : impl) {
            if ( loadedImpl != null ) {
                addHandler(loadedImpl);
            }
        }
    }

    /**
     * Add handler to available routes. Calls handler.init() before adding. The route key defaults
     * to handler.getName().
     * @param handler implementation for handling a route
     */
    public void addHandler(final Handler handler) {
        addHandler(handler.getName(), handler);
    }

    /**
     * Add handler for given route key. Calls handler.init() before adding it to available routes.
     * @param key route key
     * @param handler implementation for handling the route
     */
    public void addHandler(final String key, final Handler handler) {
        handler.init();
        routes.put(key, handler);
    }

    /**
     * Returns all route keys that are available
     * @return
     */
    public Set<String> getAvailableRoutes() {
        return routes.keySet();
    }

    /**
     * Routes the request to a handler registered to given route key or throws an RouterException
     * if the route is not registered. Also throws the exception if handler causes a RuntimeException.
     * @param key route key
     * @param params parameters passed to the handler
     * @return
     * @throws RouterException
     */
    public boolean route(final String key, final HandlerParams params) throws RouterException {
        Handler handler = routes.get(key);
        if(handler != null) {
            try {
                return handler.handleRequest(params);
            } catch(RuntimeException ex) {
                // unhandled exception in handler
                throw new RouterException("Unhandled exception on handler " + key, ex);
            }
        }
        else {
            throw new RouterException("Unknown route: " + key);
        }
    }

    /**
     * Clean up method. Calls teardown on all registered handlers.
     */
    public void teardown() {
        for( Handler h : routes.values()) {
            h.teardown();
        }
    }
}
