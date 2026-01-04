package com.javaimpl.collections.linkedhashmap;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MyLinkedHashMapTest {

    @Test
    public void testPutAndGet_expectSuccess() {
        MyLinkedHashMap<String, String> map = new MyLinkedHashMap<>();
        map.put("A", "Value A");
        map.put("B", "Value B");

        assertEquals(2, map.size());
        assertEquals("Value A", map.get("A"));
        assertEquals("Value B", map.get("B"));
    }

    @Test
    public void testInsertionOrder_expectOrderedKeys() {
        MyLinkedHashMap<String, String> map = new MyLinkedHashMap<>();
        map.put("One", "1");
        map.put("Two", "2");
        map.put("Three", "3");

        List<String> keys = map.keys();
        assertEquals(3, map.size());

        assertEquals(3, keys.size());
        assertEquals("One", keys.get(0));
        assertEquals("Two", keys.get(1));
        assertEquals("Three", keys.get(2));
    }

    @Test
    public void testUpdateKey_expectOrderUnchangedInInsertionMode() {
        MyLinkedHashMap<String, String> map = new MyLinkedHashMap<>();
        map.put("A", "1");
        map.put("B", "2");

        // Update A. In insertion-ordered map, order remains A, B.
        map.put("A", "New 1");

        List<String> keys = map.keys();
        assertEquals("A", keys.get(0));
        assertEquals("B", keys.get(1));
        assertEquals("New 1", map.get("A"));
    }

    @Test
    public void testRemove_expectGoneFromOrder() {
        MyLinkedHashMap<String, String> map = new MyLinkedHashMap<>();
        map.put("A", "1");
        map.put("B", "2");
        map.put("C", "3");

        map.remove("B");

        List<String> keys = map.keys();
        assertEquals(2, keys.size());
        assertEquals("A", keys.get(0));
        assertEquals("C", keys.get(1));
        assertNull(map.get("B"));
    }

    @Test
    public void testClear_expectEmpty() {
        MyLinkedHashMap<String, String> map = new MyLinkedHashMap<>();
        map.put("A", "1");
        map.put("B", "2");
        assertEquals(2, map.size());
        map.clear();
        assertEquals(0, map.size());
    }

    @Test
    public void testRemoveHead_expectRemoved() {
        MyLinkedHashMap<String, String> map = new MyLinkedHashMap<>();
        map.put("A", "1");
        map.put("B", "2");

        // Remove Head "A"
        map.remove("A");

        List<String> keys = map.keys();
        assertEquals(1, keys.size(), "Size should be 1");
        assertEquals("B", keys.getFirst(), "Head should be B"); // keys() uses this.head
    }

    @Test
    public void testRemovedTail_expectRemoved() {
        MyLinkedHashMap<String, String> map = new MyLinkedHashMap<>();
        map.put("A", "1");
        map.put("B", "2");

        // Remove Tail "B"
        map.remove("B");
        assertEquals(1, map.size());
        List<String> keys = map.keys();
        assertEquals("A", keys.get(0));

        // Add "C", should follow "A"
        map.put("C", "3");

        keys = map.keys();
        assertEquals(2, keys.size());
        assertEquals("A", keys.get(0));
        assertEquals("C", keys.get(1), "C should follow A");
    }

    @Test
    public void testRemovedFromSingleItemMap_expectEmptyMap() {
        MyLinkedHashMap<String, String> map = new MyLinkedHashMap<>();
        map.put("A", "1");
        assertEquals(1, map.size());
        map.remove("A");

        assertEquals(0, map.size());
        List<String> keys = map.keys();
        assertEquals(0, keys.size());
    }

    @Test
    public void testPutUpdatesAccessOrder() {
        // In AccessOrder mode, putting an existing key is considered an access
        // and should move the node to the tail.
        MyLinkedHashMap<String, String> map = new MyLinkedHashMap<>(true);
        map.put("A", "1");
        map.put("B", "2"); // Order: A, B

        map.put("A", "updated 1"); // Should be: B, A

        List<String> keys = map.keys();
        assertEquals(2, keys.size());
        assertEquals("B", keys.get(0));
        assertEquals("A", keys.get(1), "Updating key A should move it to the tail in accessOrder mode");
    }
}
