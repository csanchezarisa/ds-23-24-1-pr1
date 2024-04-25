package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import lombok.*;
import uoc.ds.pr.model.interfaces.HasId;
import uoc.ds.pr.util.Utils;

@Data
@RequiredArgsConstructor
public class Ship implements HasId {

    @NonNull
    private String id;
    @NonNull
    private String name;
    @NonNull
    private int nArmChairs;
    @NonNull
    private int nCabins2;
    @NonNull
    private int nCabins4;
    @NonNull
    private int nParkingSlots;
    @NonNull
    private int unLoadTimeInMinutes;
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
