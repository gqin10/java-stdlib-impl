package com.javaimpl.collections.hashmap;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MyHashMapTests {

    private static final int TEST_LENGTH = 1000;

    @Test
    public void testPut() {
        MyHashMap<String, String> hashMap = new MyHashMap<>();
        String result = hashMap.put("key", "value");
        assertEquals(1, hashMap.size());
        assertEquals("value", result);
        assertEquals("value", hashMap.get("key"));
    }

    @Test
    public void testGet() {
        MyHashMap<String, String> hashMap = generateHashMap();
        assertEquals(String.valueOf(TEST_LENGTH - 1), hashMap.get(String.valueOf(TEST_LENGTH - 1)));
    }

    @Test
    public void testRemove() {
        MyHashMap<String, String> hashMap = generateHashMap();
        assertEquals(TEST_LENGTH, hashMap.size());
        String result = hashMap.remove("1");
        assertEquals(TEST_LENGTH - 1, hashMap.size());
        assertEquals("1", result);
        assertNull(hashMap.get("1"));
    }

    @Test
    public void testPutExistKey() {
        MyHashMap<String, String> hashMap = new MyHashMap<>();
        hashMap.put("a", "1");
        hashMap.put("b", "2");
        assertEquals(2, hashMap.size());
        assertEquals("1", hashMap.get("a"));
        assertEquals("2", hashMap.get("b"));

        hashMap.put("b", "2 - updated");
        assertEquals(2, hashMap.size());
        assertEquals("1", hashMap.get("a"));
        assertEquals("2 - updated", hashMap.get("b"));
    }

    @Test
    public void testRemoveAbsentKey() {
        MyHashMap<String, String> hashMap = new MyHashMap<>();
        hashMap.put("a", "1");
        hashMap.put("b", "2");

        String result = hashMap.remove("absent_key");
        assertNull(result);
        assertEquals(2, hashMap.size());
    }

    private MyHashMap<String, String> generateHashMap() {
        MyHashMap<String, String> hashMap = new MyHashMap<>();
        for (int i = 0; i < TEST_LENGTH; i++) {
            hashMap.put(String.valueOf(i), String.valueOf(i));
        }
        return hashMap;
    }
}
