package uoc.ds.pr.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DSLinkedListTest {

    private DSLinkedList<String> dsLinkedList;

    @Before
    public void setUp() throws Exception {
        dsLinkedList = new DSLinkedList<>();
        dsLinkedList.insertEnd("A");
        dsLinkedList.insertEnd("B");
        dsLinkedList.insertEnd("C");
    }

    @Test
    public void findTest() {
        var findA = dsLinkedList.find("A");
        Assert.assertTrue(findA.isPresent());
        Assert.assertEquals("A", findA.get());

        var findB = dsLinkedList.find("B");
        Assert.assertTrue(findB.isPresent());
        Assert.assertEquals("B", findB.get());

        var findZ = dsLinkedList.find("Z");
        Assert.assertTrue(findZ.isEmpty());
    }

    @Test
    public void existTest() {
        Assert.assertTrue(dsLinkedList.exists("A"));
        Assert.assertTrue(dsLinkedList.exists("B"));
        Assert.assertFalse(dsLinkedList.exists("Z"));
    }

    @Test
    public void findPositionTest() {
        var positions = dsLinkedList.positions();
        Assert.assertTrue(positions.hasNext());
        var position = positions.next();

        var findA = dsLinkedList.findPosition("A");
        Assert.assertTrue(findA.isPresent());
        Assert.assertEquals(position, findA.get());

        Assert.assertTrue(positions.hasNext());
        position = positions.next();

        var findB = dsLinkedList.findPosition("B");
        Assert.assertTrue(findB.isPresent());
        Assert.assertEquals(position, findB.get());

        var findZ = dsLinkedList.findPosition("Z");
        Assert.assertTrue(findZ.isEmpty());
    }
}