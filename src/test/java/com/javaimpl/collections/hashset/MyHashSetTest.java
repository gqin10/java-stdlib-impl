package com.javaimpl.collections.hashset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyHashSetTest {

    private MyHashSet<String> set;

    @BeforeEach
    void setUp() {
        set = new MyHashSet<>();
    }

    @Test
    void testAddAndSize() {
         assertTrue(set.add("A"));
         assertTrue(set.add("B"));
         assertFalse(set.add("A")); // Duplicate
         assertEquals(2, set.size());
    }

    @Test
    void testContains() {
         set.add("X");
         assertTrue(set.contains("X"));
         assertFalse(set.contains("Y"));
    }

    @Test
    void testRemove() {
         set.add("M");
         assertTrue(set.remove("M"));
         assertFalse(set.contains("M"));
         assertFalse(set.remove("M"));
    }

    @Test
    void testClear() {
         set.add("1");
         set.add("2");
         set.clear();
         assertTrue(set.isEmpty());
         assertEquals(0, set.size());
    }

    @Test
    void testIsEmpty() {
         assertTrue(set.isEmpty());
         set.add("content");
         assertFalse(set.isEmpty());
    }
}
