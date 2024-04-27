package uoc.ds.pr.model;

import uoc.ds.pr.model.interfaces.HasId;
import uoc.ds.pr.util.DSLinkedList;

public class Ship implements HasId {

    private final String id;
    private final String name;
    private final int nArmChairs;
    private final int nCabins2;
    private final int nCabins4;
    private final int nParkingSlots;
    private final int unLoadTimeInMinutes;
    private final DSLinkedList<Voyage> voyages = new DSLinkedList<>();

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
        voyages.findPosition(voyage)
                .ifPresentOrElse(
                        p -> voyages.update(p, voyage),
                        () -> voyages.insertEnd(voyage));
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
