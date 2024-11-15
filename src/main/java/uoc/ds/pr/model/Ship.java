package uoc.ds.pr.model;

import uoc.ds.pr.util.DSLinkedList;

public class Ship {

    private final String id;
    private final String name;
    private final int nArmChairs;
    private final int nCabins2;
    private final int nCabins4;
    private final int nParkingSlots;
    private final int unLoadTimeInMinutes;
    private final DSLinkedList<Voyage> voyages = new DSLinkedList<>();

    public Ship(String id) {
        this(id, null, 0, 0, 0, 0, 0);
    }

    public Ship(String id, String name, int nArmChairs, int nCabins2, int nCabins4, int nParkingSlots, int unLoadTimeInMinutes) {
        this.id = id;
        this.name = name;
        this.nArmChairs = nArmChairs;
        this.nCabins2 = nCabins2;
        this.nCabins4 = nCabins4;
        this.nParkingSlots = nParkingSlots;
        this.unLoadTimeInMinutes = unLoadTimeInMinutes;
    }

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

    public int numVoyages() {
        return voyages.size();
    }

    public void deleteVoyage(Voyage voyage) {
        voyages.findPosition(voyage).ifPresent(voyages::delete);
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
