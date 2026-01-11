package com.javaimpl.collections.treemap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyTreeMapTest {

    private MyTreeMap<String, Integer> map;

    @BeforeEach
    public void setUp() {
        map = new MyTreeMap<>();
    }

    @Test
    public void testPutAndGet() {
        assertNull(map.put("A", 1));
        assertNull(map.put("B", 2));
        assertEquals(1, map.get("A"));
        assertEquals(2, map.get("B"));
    }

    @Test
    public void testPutExistingKey() {
        map.put("A", 10);
        assertEquals(1, map.size());
        assertEquals(10, map.get("A"));

        map.put("A", 100);
        assertEquals(1, map.size());
        assertEquals(100, map.get("A"));
    }

    @Test
    public void testSize() {
        assertEquals(0, map.size());
        map.put("A", 1);
        assertEquals(1, map.size());
        map.put("B", 2);
        assertEquals(2, map.size());
    }

    @Test
    public void testContainsKey() {
        map.put("A", 1);
        assertTrue(map.containsKey("A"));
        assertFalse(map.containsKey("B"));
    }

    @Test
    public void testRemove() {
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        assertEquals(2, map.remove("B"));
        assertNull(map.get("B"));
        assertEquals(2, map.size());

        assertNull(map.remove("D"));
    }

    @Test
    public void testClear() {
        map.put("A", 1);
        map.put("B", 2);
        map.clear();
        assertEquals(0, map.size());
        assertNull(map.get("A"));
    }

    @Test
    public void testFirstAndLastKey() {
        map.put("B", 2);
        map.put("A", 1);
        map.put("C", 3);

        assertEquals("A", map.firstKey());
        assertEquals("C", map.lastKey());
    }

    @Test
    public void testNavigationMethods() {
        map.put("1", 1);
        map.put("3", 3);
        map.put("5", 5);

        assertEquals("1", map.lowerKey("2"));
        assertEquals("3", map.lowerKey("4"));
        assertEquals("3", map.floorKey("3"));
        assertEquals("3", map.ceilingKey("3"));
        assertEquals("5", map.higherKey("4"));
    }
}
