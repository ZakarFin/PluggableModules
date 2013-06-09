package fi.zakar.annotation;

import fi.zakar.control.Handler;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.*;

/**
 * Processor for the @Route annotation. Ran on compile time and processes any @Route annotation.
 * Checks that the annotated Class is a concrete Class that is assignable as fi.zakar.control.Handler.
 * If it isn't the compilation will fail. If it is an entry is written to an SPI services file.
 * The file is created if it didn't exist and duplicates aren't written.
 */
@SupportedSourceVersion(SourceVersion.RELEASE_5)
@SupportedAnnotationTypes(RouteAnnotationProcessor.ANNOTATION_TYPE)
public class RouteAnnotationProcessor extends AbstractProcessor {
    /**
     * This is the annotation we are going to process
     */
    public static final String ANNOTATION_TYPE = "fi.zakar.annotation.Route";

    /**
     * Writing the services to an SPI file so we can find the @Routes
     * on runtime.
     * @param handlers
     * @throws IOException
     */
    private void registerControls(
            final Collection<String> handlers)
            throws IOException {

        final ServiceRegistration registration = new ServiceRegistration(
                processingEnv, "fi.zakar.control.Handler");

        registration.read(StandardLocation.SOURCE_PATH);
        registration.read(StandardLocation.CLASS_PATH);

        for (final String h : handlers) {
            registration.addClass(h);
        }
        registration.write(StandardLocation.CLASS_OUTPUT);
    }

    @Override
    public boolean process(
            final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        if (annotations == null || annotations.isEmpty()) {
            return false;
        }

        // we need the actual TypeElement of the annotation, not just it's name
        final TypeElement annotation = typeElement(ANNOTATION_TYPE);

        // make sure we have the annotation type
        if (annotation == null) {
            // no go for processing
            return false;
        }

        // get all classes that have the annotation
        final Set<? extends Element> annotatedElements =
                roundEnv.getElementsAnnotatedWith(annotation);

        try {
            // we will need to gather the annotated classes that we are
            // going to write to the services registration file
            final Set<String> results = new HashSet<String>(annotatedElements.size());

            for (final Element m : annotatedElements) {

                // we know @Route is a type annotation so casting to TypeElement
                final TypeElement el = (TypeElement) m;

                // check that the class is not abstract since we cant instantiate it if it is
                // and that its of the correct type for our fi.zakar.control.Router
                if (el.getModifiers().contains(Modifier.ABSTRACT) ||
                    !isAssignable(m.asType(), Handler.class)) {
                    // wasn't proper annotation -> go for compilation failure
                    processingEnv.getMessager().printMessage(Kind.ERROR,
                            "@Route annotated classes must be non-abstract and of type " + Handler.class, m);

                }
                else {
                    // we are good to go, gather it up in the results
                    results.add(el.getQualifiedName().toString());
                }
            }

            // write the services to file
            registerControls(results);
        } catch (final IOException ioe) {
            System.out.println("ERROR" + ioe.getMessage());
            processingEnv.getMessager().printMessage(
                    Kind.ERROR,
                    "I/O Error during processing.");

            ioe.printStackTrace();
        }

        return true;
    }

    /* ************************************
     * Convenience methods
     * ************************************
     */
    private TypeElement typeElement(Class<?> type) {
        return typeElement(type.getName());
    }
    private TypeElement typeElement(String className) {
          return processingEnv.getElementUtils().getTypeElement(className);
      }

    private boolean isAssignable(TypeElement subType, Class<?> baseType) {
        return isAssignable(subType.asType(), baseType);
    }

    private boolean isAssignable(TypeMirror subType, Class<?> baseType) {
        return isAssignable(subType, typeElement(baseType));
    }

    private boolean isAssignable(TypeMirror subType, TypeElement baseType) {
        return isAssignable(subType, baseType.asType());
    }

    private boolean isAssignable(TypeMirror subType, TypeMirror baseType) {
        final Types typeUtils = processingEnv.getTypeUtils();
        return typeUtils.isAssignable(typeUtils.erasure(subType), typeUtils.erasure(baseType));
    }

}