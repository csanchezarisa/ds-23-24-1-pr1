package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uoc.ds.pr.ShippingLine;

import java.util.Date;
import java.util.UUID;

@Data
@EqualsAndHashCode
public class Reservation {

    private final String id = UUID.randomUUID().toString();
    private LinkedList<Client> clients;
    private Voyage voyage;
    private ShippingLine.AccommodationType accommodationType;
    private String idVehicle;
    private double price;
    private boolean loaded = false;
    private Date loadedDate = null;

    public Reservation(LinkedList<Client> clients, Voyage voyage, ShippingLine.AccommodationType accommodationType, String idVehicle, double price) {
        this.clients = clients;
        this.voyage = voyage;
        this.accommodationType = accommodationType;
        this.idVehicle = idVehicle;
        this.price = price;
    }

    public Iterator<Client> clients() {
        return clients.values();
    }

    public int numClients() {
        return clients.size();
    }

    public boolean hasParkingLot() {
        return idVehicle != null && !idVehicle.isEmpty();
    }

    @Override
    public String toString() {
        return id;
    }
}
