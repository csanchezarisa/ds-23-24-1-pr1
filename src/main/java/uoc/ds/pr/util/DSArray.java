package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.FiniteContainer;
import edu.uoc.ds.adt.sequential.Queue;
import edu.uoc.ds.traversal.Iterator;
import edu.uoc.ds.traversal.IteratorArrayImpl;

import java.util.Arrays;

public class DSArray<E> implements FiniteContainer<E> {
    public static final int DEFAULT_CAPACITY = 20;

    private E[] elems;
    private int n;

    public DSArray() {
        this(DEFAULT_CAPACITY);
    }

    public DSArray(int maxElements) {
        this.elems = (E[]) new Object[maxElements];
        n = 0;
    }

    public void add(E e) {
        elems[n] = e;

        if (n != elems.length - 1)
            n++;
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

    @Override
    public boolean isFull() {
        return n >= elems.length - 1;
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
}
