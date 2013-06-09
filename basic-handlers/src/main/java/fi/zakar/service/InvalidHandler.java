package fi.zakar.service;

import fi.zakar.annotation.Route;
import fi.zakar.control.Handler;
import fi.zakar.control.HandlerParams;

/**
 * Additional dummy Route. Try changing it to abstract or not extending Handler to get a compiler error.
 */
@Route
public class InvalidHandler extends Handler {

    public boolean handleRequest(HandlerParams params) {
        System.out.println("Handling " + params.getParam("name"));
        return false;
    }
}
