package uoc.ds.pr.exceptions;

public class ShipNotFoundException extends DSException {

    public ShipNotFoundException(String shipId) {
        super("Ship not found: " + shipId);
    }
}
