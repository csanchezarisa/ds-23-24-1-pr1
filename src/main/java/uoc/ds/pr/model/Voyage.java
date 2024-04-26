package uoc.ds.pr.model;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.StackArrayImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.ShippingLine;
import uoc.ds.pr.exceptions.NoAccommodationAvailableException;
import uoc.ds.pr.exceptions.ReservationAlreadyExistsException;
import uoc.ds.pr.exceptions.ReservationNotFoundException;
import uoc.ds.pr.model.interfaces.HasId;
import uoc.ds.pr.util.FiniteLinkedList;
import uoc.ds.pr.util.Utils;

import java.util.Date;
import java.util.Optional;

public class Voyage implements HasId {

    private final LinkedList<Reservation> reservations = new LinkedList<>();
    private final FiniteLinkedList<Reservation> armChairReservations;
    private final FiniteLinkedList<Reservation> cabin2Reservations;
    private final FiniteLinkedList<Reservation> cabin4Reservations;
    private final int maxParkingReservations;
    private final StackArrayImpl<ParkingReservation> loadedParkingReservations;
    private int parkingReservations = 0;
    private String id;
    private Date departureDt;
    private Date arrivalDt;
    private Ship ship;
    private Route route;

    public Voyage(String id, Date departureDt, Date arrivalDt, Ship ship, Route route) {
        this.id = id;
        this.departureDt = departureDt;
        this.arrivalDt = arrivalDt;
        this.route = route;
        this.ship = ship;

        armChairReservations = new FiniteLinkedList<>(ship.getnArmChairs());
        cabin2Reservations = new FiniteLinkedList<>(ship.getnCabins2());
        cabin4Reservations = new FiniteLinkedList<>(ship.getnCabins4());
        maxParkingReservations = ship.getnParkingSlots();
        loadedParkingReservations = new StackArrayImpl<>(ship.getnParkingSlots());
    }

    @Override
    public String getId() {
        return id;
    }

    public Ship getShip() {
        return ship;
    }

    public Route getRoute() {
        return route;
    }

    public Date getArrivalDt() {
        return arrivalDt;
    }

    public Date getDepartureDt() {
        return departureDt;
    }

    public void addReservation(Reservation reservation) throws ReservationAlreadyExistsException, NoAccommodationAvailableException {

        validateReservation(reservation);

        switch (reservation.getAccommodationType()) {
            case ARMCHAIR -> insertArmChairReservation(reservation);
            case CABIN2 -> {
                cabin2Reservations.insertEnd(reservation);
                reservations.insertEnd(reservation);
            }
            case CABIN4 -> {
                cabin4Reservations.insertEnd(reservation);
                reservations.insertEnd(reservation);
            }
        }

        if (reservation instanceof ParkingReservation) {
            reservations.insertEnd(reservation);
            parkingReservations++;
        }
    }

    private void validateReservation(Reservation reservation) throws ReservationAlreadyExistsException, NoAccommodationAvailableException {
        if (existsReservation(reservation)) throw new ReservationAlreadyExistsException();

        boolean noAccommodationAvailable = switch (reservation.getAccommodationType()) {
            case ARMCHAIR -> reservation.getClients().size() > getAvailableArmChairs();
            case CABIN2 -> getAvailableCabin2() == 0;
            case CABIN4 -> getAvailableCabin4() == 0;
        };
        if (noAccommodationAvailable) throw new NoAccommodationAvailableException(reservation.getAccommodationType());
    }

    private void insertArmChairReservation(Reservation reservation) {
        var clients = reservation.clients();
        while (clients.hasNext()) {

            Client client = clients.next();
            Reservation newReservation = reservation.clone();

            LinkedList<Client> reservationClient = new LinkedList<>();
            reservationClient.insertEnd(client);

            newReservation.setClients(reservationClient);
            armChairReservations.insertEnd(newReservation);
            reservations.insertEnd(newReservation);
        }
    }

    public int getAvailableArmChairs() {
        return armChairReservations.length() - armChairReservations.size();
    }

    public int getAvailableCabin2() {
        return cabin2Reservations.length() - cabin2Reservations.size();
    }

    public int getAvailableCabin4() {
        return cabin4Reservations.length() - cabin4Reservations.size();
    }

    public int getAvailableParkingSlots() {
        return maxParkingReservations - parkingReservations;
    }

    public int numParkingLots() {
        return loadedParkingReservations.size();
    }

    public Iterator<Reservation> reservations() {
        return reservations.values();
    }

    public Iterator<Reservation> reservations(ShippingLine.AccommodationType accommodationType) {
        return switch (accommodationType) {
            case ARMCHAIR -> armChairReservations.values();
            case CABIN2 -> cabin2Reservations.values();
            case CABIN4 -> cabin4Reservations.values();
        };
    }

    public boolean existsReservation(Reservation reservation) {
        var clients = reservation.getClients().values();
        while (clients.hasNext()) {

            Client client = clients.next();
            var reservationIt = reservations.values();

            while (reservationIt.hasNext()) {

                Reservation oldReservation = reservationIt.next();
                if (Utils.exists(client.getId(), oldReservation.getClients().values())) {

                    return true;
                }
            }
        }

        return false;
    }

    public void loadReservation(Client client, Date loadDate) throws ReservationNotFoundException {
        Position<Reservation> reservationPos = findReservation(client)
                .orElseThrow(() -> new ReservationNotFoundException(client.getId(), id));
        Reservation reservation = reservationPos.getElem();

        if (reservation instanceof ParkingReservation parkingReservation) {
            parkingReservation.setLoadedDate(loadDate);
            loadedParkingReservations.push(parkingReservation);
        }
    }

    public Iterator<Reservation> unload() {
        LinkedList<Reservation> unLoadReservations = new LinkedList<>();
        final int shipUnloadTime = ship.getUnLoadTimeInMinutes();

        int count = 1;
        while (!loadedParkingReservations.isEmpty()) {
            ParkingReservation reservation = loadedParkingReservations.pop();
            reservation.setUnLoadTimeInMinutes(count * shipUnloadTime);
            unLoadReservations.insertEnd(reservation);
            count++;
        }

        return unLoadReservations.values();
    }

    private Optional<Position<Reservation>> findReservation(Client client) {
        var positions = reservations.positions();
        while (positions.hasNext()) {
            var pos = positions.next();
            if (Utils.exists(pos.getElem().getClients().values(), client))
                return Optional.of(pos);
        }
        return Optional.empty();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Voyage v)) return false;
        return id.equals(v.id);
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
