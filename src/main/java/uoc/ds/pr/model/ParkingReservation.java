package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import uoc.ds.pr.ShippingLine;

public class ParkingReservation extends Reservation {

    public ParkingReservation(LinkedList<Client> clients, Voyage voyage, ShippingLine.AccommodationType accommodationType, String idVehicle, double price) {
        super(clients, voyage, accommodationType, idVehicle, price);
    }

    public int getUnLoadTimeInMinutes() {
        return 0;
    }
}
