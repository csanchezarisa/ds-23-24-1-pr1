package uoc.ds.pr.model;


import edu.uoc.ds.adt.sequential.LinkedList;
import lombok.*;
import uoc.ds.pr.model.interfaces.HasId;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Route implements HasId {

    @NonNull
    private String id;
    @NonNull
    private String beginningPort;
    @NonNull
    private String arrivalPort;
    @Setter(AccessLevel.NONE)
    private LinkedList<String> voyages = new LinkedList<>();

    public void addVoyage(String voyageId) {
        voyages.insertEnd(voyageId);
    }
}
