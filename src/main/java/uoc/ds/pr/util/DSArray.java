package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.FiniteContainer;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.IteratorArrayImpl;

import java.util.Optional;

public class DSArray<E> implements FiniteContainer<E>, Findable<E> {
    public static final int DEFAULT_CAPACITY = 20;

    private final transient E[] elems;
    private int n;

    public DSArray() {
        this(DEFAULT_CAPACITY);
    }

    public DSArray(int maxElements) {
        this.elems = (E[]) new Object[maxElements];
        n = 0;
    }

    public void add(E e) {
        if (isFull()) return;

        elems[n++] = e;
    }

    public void modify(int index, E e) {
        if (index < 0 || index >= n) {
            return;
        }

        elems[index] = e;
    }

    public void addAll(E[] elems) {
        for (E e : elems) {
            add(e);
        }
    }

    public E get(int i) {
        return elems[i];
    }

    public int indexOf(E elem) {
        var elements = values();
        int index = 0;
        while (elements.hasNext()) {
            if (elements.next().equals(elem))
                return index;
            index++;
        }
        return -1;
    }

    @Override
    public boolean isFull() {
        return n >= elems.length;
    }

    @Override
    public boolean isEmpty() {
        return n == 0;
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public Iterator<E> values() {
        return new IteratorArrayImpl<>(elems, n, 0);
    }

    @Override
    public Optional<E> find(E elem) {
        var elements = values();
        while (elements.hasNext()) {
            var element = elements.next();
            if (element.equals(elem))
                return Optional.of(element);
        }
        return Optional.empty();
    }

    @Override
    public boolean exists(E elem) {
        return find(elem).isPresent();
    }
}
