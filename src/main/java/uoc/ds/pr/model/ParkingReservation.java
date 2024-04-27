package uoc.ds.pr.model;

import uoc.ds.pr.ShippingLine;
import uoc.ds.pr.util.DSLinkedList;

import java.util.Date;

public class ParkingReservation extends Reservation {

    private Date loadedDate = null;
    private Integer unLoadTimeInMinutes = null;

    public ParkingReservation(DSLinkedList<Client> clients, Voyage voyage, ShippingLine.AccommodationType accommodationType, double price, String idVehicle) {
        super(clients, voyage, accommodationType, price, idVehicle);
    }

    public void setLoadedDate(Date loadedDate) {
        this.loadedDate = loadedDate;
    }

    public int getUnLoadTimeInMinutes() {
        return unLoadTimeInMinutes;
    }

    public void setUnLoadTimeInMinutes(int unLoadTimeInMinutes) {
        this.unLoadTimeInMinutes = unLoadTimeInMinutes;
    }

    @Override
    public boolean hasParkingLot() {
        return true;
    }
}
