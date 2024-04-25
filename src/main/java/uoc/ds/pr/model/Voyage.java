package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.StackArrayImpl;
import lombok.Data;
import uoc.ds.pr.exceptions.NoAccommodationAvailableException;
import uoc.ds.pr.exceptions.ReservationAlreadyExistsException;
import uoc.ds.pr.model.interfaces.HasId;
import uoc.ds.pr.util.FiniteLinkedList;
import uoc.ds.pr.util.Utils;

import java.util.Date;

@Data
public class Voyage implements HasId {

    private final LinkedList<Reservation> reservations = new LinkedList<>();
    private final FiniteLinkedList<String> armChairReservations;
    private final FiniteLinkedList<String> cabin2Reservations;
    private final FiniteLinkedList<String> cabin4Reservations;
    private final int maxParkingReservations;
    private final StackArrayImpl<String> parkingReservations;
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

        armChairReservations = new FiniteLinkedList<>(ship.getNArmChairs());
        cabin2Reservations = new FiniteLinkedList<>(ship.getNCabins2());
        cabin4Reservations = new FiniteLinkedList<>(ship.getNCabins4());
        maxParkingReservations = ship.getNParkingSlots();
        parkingReservations = new StackArrayImpl<>(ship.getNParkingSlots());
    }

    public void addReservation(Reservation reservation) throws ReservationAlreadyExistsException, NoAccommodationAvailableException {

        validateReservation(reservation);
        reservations.insertEnd(reservation);

        switch (reservation.getAccommodationType()) {
            case ARMCHAIR -> insertArmChairReservation(reservation);
            case CABIN2 -> cabin2Reservations.insertEnd(reservation.getId());
            case CABIN4 -> cabin4Reservations.insertEnd(reservation.getId());
        }

        if (reservation.getIdVehicle() != null && !reservation.getIdVehicle().isEmpty()) {
            parkingReservations.push(reservation.getId());
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
        for (int i = 0; i < reservation.getClients().size(); i++) {
            armChairReservations.insertEnd(reservation.getId());
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
        return maxParkingReservations - parkingReservations.size();
    }

    public int numParkingLots() {
        return maxParkingReservations;
    }

    public boolean existsReservation(Reservation reservation) {
        var clients = reservation.getClients().values();
        while (clients.hasNext()) {

            Client client = clients.next();
            var reservationIt = reservations.values();

            while (reservationIt.hasNext()) {

                Reservation oldReservation = reservationIt.next();
                if (oldReservation.getVoyage().getId().equals(reservation.getVoyage().getId())
                        && Utils.exists(client.getId(), oldReservation.getClients().values())) {

                    return true;
                }
            }
        }

        return false;
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
