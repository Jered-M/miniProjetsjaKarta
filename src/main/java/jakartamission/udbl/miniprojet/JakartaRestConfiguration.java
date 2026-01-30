package jakartamission.udbl.miniprojet;

import java.util.HashSet;
import java.util.Set;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

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
        classes.add(jakartamission.udbl.miniprojet.resources.EmplacementResource.class);
        classes.add(jakartamission.udbl.miniprojet.resources.ProductResource.class);
        classes.add(jakartamission.udbl.miniprojet.resources.ReportResource.class);
        classes.add(jakartamission.udbl.miniprojet.resources.TestResource.class);
        classes.add(jakartamission.udbl.miniprojet.resources.GlobalExceptionMapper.class);
        return classes;
    }
}
