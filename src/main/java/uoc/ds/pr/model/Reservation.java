package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.ShippingLine;
import uoc.ds.pr.model.interfaces.HasId;

import java.util.UUID;

public abstract class Reservation implements HasId, Cloneable {

    private final String id = UUID.randomUUID().toString();
    private LinkedList<Client> clients;
    private Voyage voyage;
    private ShippingLine.AccommodationType accommodationType;
    private double price;
    private String idVehicle;
    private boolean loaded = false;

    protected Reservation(LinkedList<Client> clients, Voyage voyage, ShippingLine.AccommodationType accommodationType, double price, String idVehicle) {
        this.clients = clients;
        this.voyage = voyage;
        this.accommodationType = accommodationType;
        this.price = price;
        this.idVehicle = idVehicle;
    }

    @Override
    public String getId() {
        return id;
    }

    public LinkedList<Client> getClients() {
        return clients;
    }

    public void setClients(LinkedList<Client> clients) {
        this.clients = clients;
    }

    public Voyage getVoyage() {
        return voyage;
    }

    public void setVoyage(Voyage voyage) {
        this.voyage = voyage;
    }

    public ShippingLine.AccommodationType getAccommodationType() {
        return accommodationType;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public String getIdVehicle() {
        return idVehicle;
    }

    public Iterator<Client> clients() {
        return clients.values();
    }

    public int numClients() {
        return clients.size();
    }

    public double getPrice() {
        return price;
    }

    public abstract boolean hasParkingLot();

    @Override
    public String toString() {
        return id;
    }

    @Override
    public Reservation clone() {
        try {
            return (Reservation) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
