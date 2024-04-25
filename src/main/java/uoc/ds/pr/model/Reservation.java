package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.ShippingLine;
import uoc.ds.pr.model.interfaces.HasId;

import java.util.Date;
import java.util.UUID;

public class Reservation implements HasId {

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

    public void setAccommodationType(ShippingLine.AccommodationType accommodationType) {
        this.accommodationType = accommodationType;
    }

    public String getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(String idVehicle) {
        this.idVehicle = idVehicle;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public Date getLoadedDate() {
        return loadedDate;
    }

    public void setLoadedDate(Date loadedDate) {
        this.loadedDate = loadedDate;
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
