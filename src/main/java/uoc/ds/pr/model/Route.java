package uoc.ds.pr.model;


import edu.uoc.ds.adt.sequential.LinkedList;
import uoc.ds.pr.model.interfaces.HasId;
import uoc.ds.pr.util.Utils;

public class Route implements HasId {

    private final String id;
    private final String beginningPort;
    private final String arrivalPort;
    private final LinkedList<Voyage> voyages = new LinkedList<>();

    public Route(String id, String beginningPort, String arrivalPort) {
        this.id = id;
        this.beginningPort = beginningPort;
        this.arrivalPort = arrivalPort;
    }

    @Override
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
        Utils.findPosition(voyages.positions(), voyage)
                .ifPresentOrElse(
                        p -> voyages.update(p, voyage),
                        () -> voyages.insertEnd(voyage));
    }

    public int numVoyages() {
        return voyages.size();
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
