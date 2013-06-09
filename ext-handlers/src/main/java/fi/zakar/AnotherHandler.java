package fi.zakar;

import fi.zakar.annotation.Route;
import fi.zakar.control.Handler;
import fi.zakar.control.HandlerParams;

/**
 * Dummy handler that is going to be registered for route key "AnotherHandler".
 */
@Route
public class AnotherHandler extends Handler {

    public boolean handleRequest(HandlerParams params) {
        System.out.println(getName() + "Handling " + params.getParam("name"));
        return true;
    }


}