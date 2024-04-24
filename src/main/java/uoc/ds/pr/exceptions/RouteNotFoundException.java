package uoc.ds.pr.exceptions;

public class RouteNotFoundException extends DSException {

    public RouteNotFoundException(String routeId) {
        super("Route not found: " + routeId);
    }
}
