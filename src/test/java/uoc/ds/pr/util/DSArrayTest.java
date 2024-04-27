package uoc.ds.pr.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DSArrayTest {
    private static final int MAX_ELEMENTS = 5;
    private DSArray<String> dsArray;

    @Before
    public void setUp() {
        dsArray = new DSArray<>(MAX_ELEMENTS);
        dsArray.add("A");
        dsArray.add("B");
        dsArray.add("C");
    }

    @Test
    public void isFullAndIsEmptyTest() {
        Assert.assertFalse(dsArray.isEmpty());
        Assert.assertFalse(dsArray.isFull());

        dsArray.add("D");
        dsArray.add("E");

        Assert.assertFalse(dsArray.isEmpty());
        Assert.assertTrue(dsArray.isFull());

        dsArray = new DSArray<>(MAX_ELEMENTS);

        Assert.assertTrue(dsArray.isEmpty());
        Assert.assertFalse(dsArray.isFull());
    }

    @Test
    public void valuesTest() {
        var elements = dsArray.values();

        Assert.assertTrue(elements.hasNext());
        Assert.assertEquals("A", elements.next());
        Assert.assertTrue(elements.hasNext());
        Assert.assertEquals("B", elements.next());
        Assert.assertTrue(elements.hasNext());
        Assert.assertEquals("C", elements.next());
        Assert.assertFalse(elements.hasNext());
    }

    @Test
    public void findAndExistTest() {
        Assert.assertTrue(dsArray.exists("A"));
        var element = dsArray.find("A");
        Assert.assertTrue(element.isPresent());
        Assert.assertEquals("A", element.get());

        Assert.assertTrue(dsArray.exists("C"));
        element = dsArray.find("C");
        Assert.assertTrue(element.isPresent());
        Assert.assertEquals("C", element.get());

        Assert.assertFalse(dsArray.exists("Z"));
        element = dsArray.find("Z");
        Assert.assertTrue(element.isEmpty());
    }

    @Test
    public void sizeTest() {
        Assert.assertEquals(3, dsArray.size());

        dsArray.add("D");
        dsArray.add("E");
        Assert.assertEquals(5, dsArray.size());

        dsArray = new DSArray<>(MAX_ELEMENTS);
        Assert.assertEquals(0, dsArray.size());
    }

    @Test
    public void getAndIndexOfAndModifyTest() {
        Assert.assertEquals(0, dsArray.indexOf("A"));
        Assert.assertEquals("A", dsArray.get(0));

        dsArray.modify(0, "A2");
        Assert.assertEquals(0, dsArray.indexOf("A2"));
        Assert.assertEquals("A2", dsArray.get(0));

        Assert.assertEquals(1, dsArray.indexOf("B"));
        Assert.assertEquals("B", dsArray.get(1));

        Assert.assertEquals(-1, dsArray.indexOf("Z"));
        Assert.assertEquals(-1, dsArray.indexOf("A"));
    }

    @Test
    public void addAllTest() {
        String[] elems = new String[]{"D", "E", "F"};
        dsArray.addAll(elems);

        Assert.assertTrue(dsArray.isFull());
        Assert.assertEquals(MAX_ELEMENTS, dsArray.size());
        Assert.assertFalse(dsArray.exists("F"));
        Assert.assertEquals("E", dsArray.get(4));
    }
}