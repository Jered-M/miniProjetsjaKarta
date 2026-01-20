package jakartamission.udbl.miniprojet;

import java.util.HashSet;
import java.util.Set;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import jakartamission.udbl.miniprojet.resources.DemoResource;

/**
 * Configures Jakarta RESTful Web Services for the application.
 * 
 * @author Juneau
 */
@ApplicationPath("api")
public class JakartaRestConfiguration extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(DemoResource.class);
        return classes;
    }
}
