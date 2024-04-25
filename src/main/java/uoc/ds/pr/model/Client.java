package uoc.ds.pr.model;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;
import lombok.*;
import uoc.ds.pr.exceptions.LoadingAlreadyException;
import uoc.ds.pr.exceptions.ReservationNotFoundException;
import uoc.ds.pr.model.interfaces.HasId;

import java.util.Optional;

@Data
@RequiredArgsConstructor
public class Client implements HasId {

    @NonNull
    private String id;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @Setter(AccessLevel.NONE)
    private LinkedList<String> voyages = new LinkedList<>();
    @Setter(AccessLevel.NONE)
    private LinkedList<Reservation> reservations = new LinkedList<>();

    public void addVoyage(String voyageId) {
        voyages.insertEnd(voyageId);
    }

    public void addReservation(Reservation reservation) {
        reservations.insertEnd(reservation);
    }

    public int countLoadedReservations() {
        int count = 0;

        var positions = reservations.positions();
        while (positions.hasNext()) {
            var reservation = positions.next().getElem();
            if (reservation.isLoaded()) count++;
        }

        return count;
    }

    public Iterator<Reservation> reservations() {
        return reservations.values();
    }

    public boolean containsReservation(Voyage voyage) {
        return findReservation(voyage).isPresent();
    }

    public void loadReservation(Voyage voyage) throws ReservationNotFoundException, LoadingAlreadyException {
        Position<Reservation> reservationPos = findReservation(voyage)
                .orElseThrow(() -> new ReservationNotFoundException(id, voyage.getId()));
        Reservation reservation = reservationPos.getElem();

        if (reservation.isLoaded()) throw new LoadingAlreadyException(id, voyage.getId());

        reservation.setLoaded(true);
        reservations.update(reservationPos, reservation);
    }

    private Optional<Position<Reservation>> findReservation(Voyage voyage) {
        var positions = reservations.positions();
        while (positions.hasNext()) {
            var pos = positions.next();
            if (voyage.equals(pos.getElem().getVoyage()))
                return Optional.of(pos);
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Client c)) return false;
        return id.equals(c.id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }
}
