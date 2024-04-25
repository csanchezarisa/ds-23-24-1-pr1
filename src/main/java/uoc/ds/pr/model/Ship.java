package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import uoc.ds.pr.model.interfaces.HasId;
import uoc.ds.pr.util.Utils;

public class Ship implements HasId {

    private String id;
    private String name;
    private int nArmChairs;
    private int nCabins2;
    private int nCabins4;
    private int nParkingSlots;
    private int unLoadTimeInMinutes;
    private LinkedList<Voyage> voyages = new LinkedList<>();

    public Ship(String id, String name, int nArmChairs, int nCabins2, int nCabins4, int nParkingSlots, int unLoadTimeInMinutes) {
        this.id = id;
        this.name = name;
        this.nArmChairs = nArmChairs;
        this.nCabins2 = nCabins2;
        this.nCabins4 = nCabins4;
        this.nParkingSlots = nParkingSlots;
        this.unLoadTimeInMinutes = unLoadTimeInMinutes;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getnArmChairs() {
        return nArmChairs;
    }

    public int getnCabins2() {
        return nCabins2;
    }

    public int getnCabins4() {
        return nCabins4;
    }

    public int getnParkingSlots() {
        return nParkingSlots;
    }

    public int getUnLoadTimeInMinutes() {
        return unLoadTimeInMinutes;
    }

    public void addVoyage(Voyage voyage) {

        var position = Utils.findPosition(voyages.positions(), voyage);
        if (position.isEmpty()) {
            voyages.insertEnd(voyage);
        } else {
            voyages.update(position.get(), voyage);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ship s)) return false;
        return id.equals(s.id);
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
