package uoc.ds.pr.util;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.LinkedList;

import java.util.Optional;

public class DSLinkedList<E> extends LinkedList<E> implements Findable<E> {

    public Optional<Position<E>> findPosition(E elem) {
        var positions = positions();
        while (positions.hasNext()) {
            var position = positions.next();
            if (position.getElem().equals(elem))
                return Optional.of(position);
        }
        return Optional.empty();
    }

    @Override
    public Optional<E> find(E elem) {
        return findPosition(elem).map(Position::getElem);
    }

    @Override
    public boolean exists(E elem) {
        return findPosition(elem).isPresent();
    }
}
