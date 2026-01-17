package jakartamission.udbl.miniprojet;

import java.util.HashSet;
import java.util.Set;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import jakartamission.udbl.miniprojet.resources.EmplacementResource;
import jakartamission.udbl.miniprojet.resources.JakartaEE10Resource;
import jakartamission.udbl.miniprojet.resources.ReportResource;

/**
 * Configures Jakarta RESTful Web Services for the application.
 * 
 * @author Juneau
 */
@ApplicationPath("resources")
public class JakartaRestConfiguration extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(JakartaEE10Resource.class);
        classes.add(EmplacementResource.class);
        classes.add(ReportResource.class);
        return classes;
    }
}
