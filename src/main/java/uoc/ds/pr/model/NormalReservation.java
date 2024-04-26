package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import uoc.ds.pr.ShippingLine;

public class NormalReservation extends Reservation {

    public NormalReservation(LinkedList<Client> clients, Voyage voyage, ShippingLine.AccommodationType accommodationType, double price, String idVehicle) {
        super(clients, voyage, accommodationType, price, idVehicle);
    }

    @Override
    public boolean hasParkingLot() {
        return false;
    }
}
