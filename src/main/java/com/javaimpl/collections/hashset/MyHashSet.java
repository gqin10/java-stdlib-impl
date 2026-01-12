package com.javaimpl.collections.hashset;

import com.javaimpl.collections.hashmap.MyHashMap;

/**
 * A custom implementation of HashSet.
 * This class implements the Set interface, backed by a hash table (actually a MyHashMap instance).
 *
 * @param <E> the type of elements maintained by this set
 */
public class MyHashSet<E> {

    private transient MyHashMap<E, Object> map;

    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();

    /**
     * Constructs a new, empty set; the backing HashMap instance has
     * default initial capacity (16) and load factor (0.75).
     */
    public MyHashSet() {
        map = new MyHashMap<>();
    }

    public MyHashSet(int initialCapacity, float loadFactor) {
        map = new MyHashMap<>(initialCapacity, loadFactor);
    }

    public int size() {
        return map.size();
    }

    public boolean contains(E obj) {
        return map.get(obj) != null;
    }

    public boolean add(E obj) {
        return map.put(obj, PRESENT) == null;
    }

    public boolean remove(E obj) {
        return map.remove(obj) == PRESENT;
    }

    public void clear() {
        map.clear();
    }

    public boolean isEmpty() {
        return map.size() == 0;
    }
}
