package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import uoc.ds.pr.ShippingLine;

import java.util.Date;

public class ParkingReservation extends Reservation {

    private Date loadedDate = null;
    private Integer unLoadTimeInMinutes = null;

    public ParkingReservation(LinkedList<Client> clients, Voyage voyage, ShippingLine.AccommodationType accommodationType, double price, String idVehicle) {
        super(clients, voyage, accommodationType, price, idVehicle);
    }

    public Date getLoadedDate() {
        return loadedDate;
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
