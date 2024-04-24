package uoc.ds.pr.exceptions;

public class LandingNotDoneException extends DSException {

    public LandingNotDoneException(String voyageId) {
        super("Landing not done in voyage: " + voyageId);
    }
}
