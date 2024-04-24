package uoc.ds.pr.model;

import edu.uoc.ds.adt.sequential.LinkedList;
import lombok.*;
import uoc.ds.pr.model.interfaces.HasId;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
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
    private int  nParkingLots;
    @NonNull
    private int unloadTime;
    @Setter(AccessLevel.NONE)
    private LinkedList<String> voyages = new LinkedList<>();

    public void addVoyage(String voyageId) {
        voyages.insertEnd(voyageId);
    }
}
