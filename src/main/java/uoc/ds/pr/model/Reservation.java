package uoc.ds.pr.model;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.ShippingLine;
import uoc.ds.pr.model.interfaces.HasId;
import uoc.ds.pr.util.DSLinkedList;

import java.util.Objects;
import java.util.UUID;

public abstract class Reservation implements HasId, Cloneable {

    private final String id = UUID.randomUUID().toString();
    private DSLinkedList<Client> clients;
    private Voyage voyage;
    private final ShippingLine.AccommodationType accommodationType;
    private final double price;
    private final String idVehicle;
    private boolean loaded = false;

    protected Reservation(DSLinkedList<Client> clients, Voyage voyage, ShippingLine.AccommodationType accommodationType, double price, String idVehicle) {
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

    public void setClients(DSLinkedList<Client> clients) {
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

    public abstract boolean hasParkingLot();

    public boolean containsClient(Client client) {
        return clients.exists(client);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Reservation r)) return false;
        return id.equals(r.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

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
