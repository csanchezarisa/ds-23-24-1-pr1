package uoc.ds.pr.model;

import uoc.ds.pr.util.DSLinkedList;

public class Route {

    private final String id;
    private final String beginningPort;
    private final String arrivalPort;
    private final DSLinkedList<Voyage> voyages = new DSLinkedList<>();

    public Route(String id) {
        this(id, null, null);
    }

    public Route(String id, String beginningPort, String arrivalPort) {
        this.id = id;
        this.beginningPort = beginningPort;
        this.arrivalPort = arrivalPort;
    }

    public String getId() {
        return id;
    }

    public String getBeginningPort() {
        return beginningPort;
    }

    public String getArrivalPort() {
        return arrivalPort;
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
        if (!(o instanceof Route r)) return false;
        return id.equals(r.id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return beginningPort + "-" + arrivalPort;
    }
}
