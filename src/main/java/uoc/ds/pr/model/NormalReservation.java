package uoc.ds.pr.model;

import uoc.ds.pr.ShippingLine;
import uoc.ds.pr.util.DSLinkedList;

public class NormalReservation extends Reservation {

    public NormalReservation(DSLinkedList<Client> clients, Voyage voyage, ShippingLine.AccommodationType accommodationType, double price, String idVehicle) {
        super(clients, voyage, accommodationType, price, idVehicle);
    }

    @Override
    public boolean hasParkingLot() {
        return false;
    }
}
