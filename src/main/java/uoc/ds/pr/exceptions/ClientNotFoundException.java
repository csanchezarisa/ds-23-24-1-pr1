package uoc.ds.pr.exceptions;

public class ClientNotFoundException extends DSException {

    public ClientNotFoundException(String cliendId) {
        super("Client not found: " + cliendId);
    }
}
