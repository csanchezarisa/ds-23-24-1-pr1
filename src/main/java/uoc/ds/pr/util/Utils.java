package uoc.ds.pr.util;

import edu.uoc.ds.traversal.Iterator;

public final class Utils {

    public static <T> boolean exists(Iterator<T> elements, T elem) {
        while (elements.hasNext()) {
            if (elements.next().equals(elem))
                return true;
        }
        return false;
    }

    private Utils() {
        throw new UnsupportedOperationException("This is a utility class and should not be initialized");
    }
}
