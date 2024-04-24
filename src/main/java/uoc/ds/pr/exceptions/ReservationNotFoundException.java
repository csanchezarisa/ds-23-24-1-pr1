package uoc.ds.pr.exceptions;

public class ReservationNotFoundException extends DSException {

    public ReservationNotFoundException(String clientId, String voyageId) {
        super(String.format("Reservation for client=%s and voyage=%s not found", clientId, voyageId));
    }
}
