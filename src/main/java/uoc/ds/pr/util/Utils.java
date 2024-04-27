package uoc.ds.pr.util;

import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.model.interfaces.HasId;

import java.util.Optional;

public final class Utils {

    public static <T> boolean exists(Iterator<T> elements, T elem) {
        while (elements.hasNext()) {
            if (elements.next().equals(elem))
                return true;
        }
        return false;
    }

    public static <T extends HasId> Optional<T> find(String id, Iterator<T> elements) {
        while (elements.hasNext()) {
            var element = elements.next();
            if (element.getId().equals(id))
                return Optional.of(element);
        }
        return Optional.empty();
    }

    private Utils() {
        throw new UnsupportedOperationException("This is a utility class and should not be initialized");
    }
}
