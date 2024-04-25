package uoc.ds.pr.util;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.FiniteContainer;
import edu.uoc.ds.adt.sequential.LinkedList;

import java.util.Optional;

public class FiniteLinkedList<E> extends LinkedList<E> implements FiniteContainer<E> {
    public static final int DEFAULT_CAPACITY = 20;
    private final int max;

    public FiniteLinkedList() {
        this(DEFAULT_CAPACITY);
    }

    public FiniteLinkedList(int  max) {
        this.max = max;
    }

    @Override
    public Position<E> insertBeginning(E elem) {
        if (isFull())
            return null;

        return super.insertBeginning(elem);
    }

    @Override
    public Position<E> insertEnd(E elem) {
        if (isFull())
            return null;

        return super.insertEnd(elem);
    }

    @Override
    public Position<E> insertBefore(Position<E> node, E elem) {
        if (isFull())
            return null;

        return super.insertBefore(node, elem);
    }

    @Override
    public Position<E> insertAfter(Position<E> node, E elem) {
        if (isFull())
            return null;

        return super.insertAfter(node, elem);
    }

    @Override
    public boolean isFull() {
        return size() >= max;
    }

    public int length() {
        return max;
    }

    public Optional<Position<E>> find(E elem) {
        var elems = positions();
        while (elems.hasNext()) {
            var e = elems.next();
            if (e.getElem().equals(elem))
                return Optional.of(e);
        }
        return Optional.empty();
    }
}
