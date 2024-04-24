package uoc.ds.pr.exceptions;

public class VehicleNotFoundException extends DSException {

    public VehicleNotFoundException(String vehicleId) {
        super("Vehicle not found: " + vehicleId);
    }
}
