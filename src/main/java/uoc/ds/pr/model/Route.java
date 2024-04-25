package uoc.ds.pr.model;


import edu.uoc.ds.adt.sequential.LinkedList;
import lombok.*;
import uoc.ds.pr.model.interfaces.HasId;
import uoc.ds.pr.util.Utils;

@Data
@RequiredArgsConstructor
public class Route implements HasId {

    @NonNull
    private String id;
    @NonNull
    private String beginningPort;
    @NonNull
    private String arrivalPort;
    @Setter(AccessLevel.NONE)
    private LinkedList<Voyage> voyages = new LinkedList<>();

    public void addVoyage(Voyage voyage) {
        var position = Utils.findPosition(voyages.positions(), voyage);
        if (position.isEmpty()) {
            voyages.insertEnd(voyage);
        } else {
            voyages.update(position.get(), voyage);
        }
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
        return id;
    }
}
