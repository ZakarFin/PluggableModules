import fi.zakar.control.HandlerException;
import fi.zakar.control.HandlerParams;
import fi.zakar.control.Router;
import fi.zakar.control.RouterException;

/**
 * Main class to test the annotation parsing, service registration and Router that ultimately
 * uses these.
 */
public class App {

    private Router router = null;


    public static void main(String[] args) {
        App u = new App();
        u.callServices();
    }

    public App() {
        router = new Router();
    }


    public void callServices() {

        System.out.println("start");
        HandlerParams p = new HandlerParams();

        for( String key : router.getAvailableRoutes()) {
            System.out.println("Calling route " + key);
            try {
                if(!router.route(key, p)) {
                    System.out.println("Handler wasnt happy");
                }
            } catch (HandlerException ex) {
                System.out.println("Handler reported exception");
            } catch (RouterException ex) {
                System.out.println("Something went terribly wrong");
            }
        }
        System.out.println("done");
    }
}
