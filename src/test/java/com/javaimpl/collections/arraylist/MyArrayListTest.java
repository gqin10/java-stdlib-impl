package com.javaimpl.collections.arraylist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MyArrayListTest {

    @Test
    public void testAdd() {
        MyArrayList<String> list = new MyArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        Assertions.assertEquals(3, list.size());
    }

    @Test
    public void testInsertAtIndex() {
        MyArrayList<String> list = new MyArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("z", 2);
        Assertions.assertEquals(4, list.size());
        Assertions.assertEquals("z", list.get(2));
    }

    @Test
    public void testRemove() {
        MyArrayList<String> list = new MyArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        Assertions.assertEquals(3, list.size());
        Assertions.assertEquals("b", list.get(1));
        list.remove(1);
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("c", list.get(1));
    }

    @Test
    public void testResizing() {
        MyArrayList<Integer> list = new MyArrayList<>();
        // Default capacity is 10. Adding 20 elements forces resize.
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        Assertions.assertEquals(20, list.size());
        for (int i = 0; i < 20; i++) {
            Assertions.assertEquals(i, list.get(i));
        }
    }

    @Test
    public void testBoundsChecking() {
        MyArrayList<String> list = new MyArrayList<>();
        list.add("a");

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.get(10));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @Test
    public void testEdgeCases() {
        MyArrayList<String> list = new MyArrayList<>();
        list.add("Start");
        list.add("Middle");
        list.add("End");

        // Remove Last
        list.remove(2);
        Assertions.assertEquals(2, list.size());
        Assertions.assertEquals("Middle", list.get(1));

        // Remove First
        list.remove(0);
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals("Middle", list.get(0));
    }

}