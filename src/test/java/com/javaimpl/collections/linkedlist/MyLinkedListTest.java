package com.javaimpl.collections.linkedlist;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedListTest {

    @Test
    public void testAddAndGet_expectSuccess() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
    }

    @Test
    public void testAddFirst_expectAtFirstElement() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("Second");
        list.addFirst("First");

        assertEquals(2, list.size());
        assertEquals("First", list.get(0));
        assertEquals("Second", list.get(1));
    }

    @Test
    public void testAddLast_expectAtLastElement() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("First");
        list.addLast("Last");

        assertEquals(2, list.size());
        assertEquals("First", list.get(0));
        assertEquals("Last", list.get(1));
    }

    @Test
    public void testAddAtMiddle_expectAtMiddle() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("C");
        list.add("Middle", 1);

        assertEquals("A", list.get(0));
        assertEquals("Middle", list.get(1));
        assertEquals("C", list.get(2));
        assertEquals(3, list.size());
    }

    @Test
    public void testAddAtHead_expectAtFirst() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("B");
        list.add("C");
        list.add("Start", 0);

        assertEquals("Start", list.get(0));
        assertEquals("B", list.get(1));
        assertEquals("C", list.get(2));
        assertEquals(3, list.size());
    }

    @Test
    public void testAddAtTail_expectAtLast() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");
        list.add("End", 2);

        assertEquals("End", list.get(2));
        assertEquals(3, list.size());
    }

    @Test
    public void testRemoveMiddle_expectSuccess() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        String removed = list.remove(1);
        assertEquals("B", removed);
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
    }

    @Test
    public void testRemoveHead_expectSuccess() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        String removed = list.remove(0);
        assertEquals("A", removed);
        assertEquals(2, list.size());
        assertEquals("B", list.get(0));
        assertEquals("C", list.get(1));
    }

    @Test
    public void testRemoveTail_expectSuccess() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        String removed = list.remove(2);
        assertEquals("C", removed);
        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
    }

    @Test
    public void testRemoveByObject_expectSuccess() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        // Remove existing
        String removed = list.remove("B");
        assertEquals("B", removed);
        assertEquals(2, list.size());
        assertFalse(list.contains("B"));
    }

    @Test
    public void testRemoveNonExist_expectNothingRemoved() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("A");

        // Remove non-existing
        String removed = list.remove("Z");
        assertNull(removed);
        assertEquals(1, list.size());
    }

    @Test
    public void testRemoveInvalidIndex_expectNoRemoved() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("A");
        String removed = list.remove(1);
        assertNull(removed);
    }

    @Test
    public void testSet_expectValueUpdated() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");

        list.set(1, "Changed");
        assertEquals("Changed", list.get(1));

        list.set(0, "Start");
        assertEquals("Start", list.get(0));
    }

    @Test
    public void testSetInvalidIndex_expectException() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("A");
        list.set(0, "A2");
        assertThrows(NoSuchElementException.class, () -> list.set(1, "Changed"));
    }

    @Test
    public void testContains_valueExist_expectFound() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("FindMe");
        assertTrue(list.contains("FindMe"));
    }

    @Test
    public void testContains_valueNotExist_expectNotFound() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("FindMe");
        assertFalse(list.contains("invalid_value"));
    }

    @Test
    public void testClear_expectSuccess() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("A");
        list.add("B");
        assertEquals(2, list.size());

        list.clear();
        assertEquals(0, list.size());
        assertNull(list.get(0));
        assertFalse(list.contains("A"));

        // Ensure we can add again after clear
        list.add("New");
        assertEquals(1, list.size());
        assertEquals("New", list.get(0));
    }
}
