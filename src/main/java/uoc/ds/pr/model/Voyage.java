package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.StackArrayImpl;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.ShippingLine;
import uoc.ds.pr.exceptions.NoAccommodationAvailableException;
import uoc.ds.pr.exceptions.ParkingFullException;
import uoc.ds.pr.exceptions.ReservationAlreadyExistsException;
import uoc.ds.pr.exceptions.ReservationNotFoundException;
import uoc.ds.pr.model.interfaces.HasId;
import uoc.ds.pr.util.DSLinkedList;
import uoc.ds.pr.util.FiniteLinkedList;
import uoc.ds.pr.util.Utils;

import java.util.Date;
import java.util.Optional;

public class Voyage implements HasId {

    private final DSLinkedList<Reservation> reservations = new DSLinkedList<>();
    private final FiniteLinkedList<Reservation> armChairReservations;
    private final FiniteLinkedList<Reservation> cabin2Reservations;
    private final FiniteLinkedList<Reservation> cabin4Reservations;
    private final int maxParkingReservations;
    private final StackArrayImpl<ParkingReservation> loadedParkingReservations;
    private int parkingReservations = 0;
    private final String id;
    private final Date departureDt;
    private final Date arrivalDt;
    private final Ship ship;
    private final Route route;
    private boolean landingDone = false;

    public Voyage(String id) {
        this.id = id;
        this.armChairReservations = null;
        this.cabin2Reservations = null;
        this.cabin4Reservations = null;
        this.maxParkingReservations = 0;
        this.loadedParkingReservations = null;
        this.departureDt = null;
        this.arrivalDt = null;
        this.ship = null;
        this.route = null;
    }

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

    public boolean isLandingDone() {
        return landingDone;
    }

    public void addReservation(Reservation reservation) throws ReservationAlreadyExistsException, NoAccommodationAvailableException, ParkingFullException {

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
            parkingReservations++;
        }
    }

    private void validateReservation(Reservation reservation) throws ReservationAlreadyExistsException, NoAccommodationAvailableException, ParkingFullException {
        if (existsReservation(reservation)) throw new ReservationAlreadyExistsException();

        boolean noAccommodationAvailable = switch (reservation.getAccommodationType()) {
            case ARMCHAIR -> reservation.numClients() > getAvailableArmChairs();
            case CABIN2 -> getAvailableCabin2() == 0;
            case CABIN4 -> getAvailableCabin4() == 0;
        };
        if (noAccommodationAvailable) throw new NoAccommodationAvailableException(reservation.getAccommodationType());

        if (reservation.hasParkingLot() && getAvailableParkingSlots() == 0) throw new ParkingFullException();
    }

    private void insertArmChairReservation(Reservation reservation) {
        var clients = reservation.clients();
        while (clients.hasNext()) {

            Client client = clients.next();
            Reservation newReservation = reservation.clone();

            DSLinkedList<Client> reservationClient = new DSLinkedList<>();
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

    public Optional<Reservation> vehicleReservation(String idVehicle) {
        var r = this.reservations.values();

        while (r.hasNext()) {
            Reservation reservation = r.next();
            if (reservation.hasParkingLot() && reservation.getIdVehicle().equals(idVehicle)) {
                return Optional.of(reservation);
            }
        }

        return Optional.empty();
    }

    public boolean existsReservation(Reservation reservation) {
        var clients = reservation.clients();
        while (clients.hasNext()) {

            Client client = clients.next();
            var reservationIt = reservations.values();

            while (reservationIt.hasNext()) {
                Reservation oldReservation = reservationIt.next();
                if (oldReservation.containsClient(client)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void loadReservation(Client client, Date loadDate) throws ReservationNotFoundException {
        Reservation reservation = findReservation(client)
                .orElseThrow(() -> new ReservationNotFoundException(client.getId(), id));

        if (reservation instanceof ParkingReservation parkingReservation
                && !Utils.exists(loadedParkingReservations.values(), parkingReservation)) {
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

        landingDone = true;

        return unLoadReservations.values();
    }

    private Optional<Reservation> findReservation(Client client) {
        var reservationIt = reservations.values();
        while (reservationIt.hasNext()) {
            var reservation = reservationIt.next();
            if (reservation.containsClient(client))
                return Optional.of(reservation);
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
