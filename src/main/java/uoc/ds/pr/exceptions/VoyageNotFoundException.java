package uoc.ds.pr.exceptions;

public class VoyageNotFoundException extends DSException {

    public VoyageNotFoundException(String voyageId) {
        super("Voyage not found: " + voyageId);
    }
}
