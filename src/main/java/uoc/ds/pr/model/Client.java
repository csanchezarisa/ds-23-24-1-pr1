package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;
import lombok.*;
import uoc.ds.pr.model.interfaces.HasId;

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
