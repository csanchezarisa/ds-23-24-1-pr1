package uoc.ds.pr.exceptions;

public class LoadingAlreadyException extends DSException {

    public LoadingAlreadyException(String clientId, String voyageId) {
        super(String.format("client=%s is already loading in voyage=%s", clientId, voyageId));
    }
}
