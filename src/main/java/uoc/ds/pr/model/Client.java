package uoc.ds.pr.model;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.exceptions.LoadingAlreadyException;
import uoc.ds.pr.exceptions.ReservationNotFoundException;
import uoc.ds.pr.model.interfaces.HasId;
import uoc.ds.pr.util.DSLinkedList;

import java.util.Optional;

public class Client implements HasId {

    private final String id;
    private final String name;
    private final String surname;
    private final DSLinkedList<Voyage> voyages = new DSLinkedList<>();
    private final DSLinkedList<Reservation> reservations = new DSLinkedList<>();

    public Client(String id) {
        this(id, null, null);
    }

    public Client(String id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void addVoyage(Voyage voyage) {
        voyages.insertEnd(voyage);
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
