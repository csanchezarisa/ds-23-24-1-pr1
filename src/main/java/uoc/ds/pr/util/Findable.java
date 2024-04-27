package uoc.ds.pr.util;

import java.util.Optional;

public interface Findable<E> {

    Optional<E> find(E elem);

    boolean exists(E elem);
}
