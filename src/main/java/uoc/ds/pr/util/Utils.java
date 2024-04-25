package uoc.ds.pr.util;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.Traversal;
import lombok.experimental.UtilityClass;
import uoc.ds.pr.model.interfaces.HasId;

import java.util.Optional;

@UtilityClass
public final class Utils {

    public static <T extends HasId> boolean exists(String id, Iterator<T> elements) {
        return findIndex(id, elements) != -1;
    }

    public static <T> boolean exists(Iterator<T> elements, T elem) {
        return findIndex(elements, elem) != -1;
    }

    public static <T extends HasId> T find(String id, Iterator<T> elements) {
        while (elements.hasNext()) {
            var element = elements.next();
            if (element.getId().equals(id))
                return element;
        }
        return null;
    }

    public static <T extends HasId> int findIndex(String id, Iterator<T> elements) {
        int index = 0;
        while (elements.hasNext()) {
            if (elements.next().getId().equals(id))
                return index;
            index++;
        }
        return -1;
    }

    public static <T> int findIndex(Iterator<T> elements, T elem) {
        int index = 0;
        while (elements.hasNext()) {
            if (elements.next().equals(elem))
                return index;
            index++;
        }
        return -1;
    }

    public static <T extends HasId> Optional<Position<T>> findPosition(String id, Traversal<T> positions) {
        while (positions.hasNext()) {
            var position = positions.next();
            if (position.getElem().getId().equals(id))
                return Optional.of(position);
        }
        return Optional.empty();
    }

    public static <T> Optional<Position<T>> findPosition(Traversal<T> positions, T elem) {
        while (positions.hasNext()) {
            var position = positions.next();
            if (position.getElem().equals(elem))
                return Optional.of(position);
        }
        return Optional.empty();
    }
}
