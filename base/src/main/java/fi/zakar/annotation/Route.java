package fi.zakar.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Route annotation is used to detect Handler services. At compile time the annotated
 * Handlers are registered as SPI services and on runtime the value of the annotation is used
 * as the router key when registering it as a handler.
 * @see fi.zakar.control.Handler
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Route {

    /**
     * Returns the key which should be used when registering the route
     * @return
     */
    String value() default "";
}
