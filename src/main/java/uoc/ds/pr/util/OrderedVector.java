package uoc.ds.pr.util;

import edu.uoc.ds.adt.helpers.Position;

import java.util.Comparator;

public class OrderedVector<E> extends FiniteLinkedList<E> {

    private final transient Comparator<E> cmp;

    public OrderedVector(Comparator<E> cmp) {
        this.cmp = cmp;
    }

    public OrderedVector(int max, Comparator<E> cmp) {
        super(max);
        this.cmp = cmp;
    }

    public E update(E elem) {
        if (isFull()) {
            if (cmp.compare(elem, last.getElem()) <= 0)
                return last.getElem();

            delete(last);
        }

        if (last == null || !last.getElem().equals(elem) && cmp.compare(elem, last.getElem()) <= 0) {
            return super.insertEnd(elem).getElem();
        }

        var elems = positions();
        Position<E> curr = null;

        while (elems.hasNext()) {
            curr = elems.next();
            if (cmp.compare(elem, curr.getElem()) >= 0)
                break;
        }

        return super.insertBefore(curr, elem).getElem();
    }

    public E delete(E elem) {
        return findPosition(elem).map(super::delete).orElse(null);
    }
}
