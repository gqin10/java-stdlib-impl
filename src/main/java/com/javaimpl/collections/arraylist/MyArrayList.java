package com.javaimpl.collections.arraylist;

import java.util.Arrays;

/**
 *
 * @param <S> class of stored object
 */
public class MyArrayList<S> {

    private static final int DEFAULT_CAPACITY = 10;

    private static final Object[] EMPTY_ELEMENTDATA = {};

    private Object[] list;

    private int size;

    public MyArrayList() {
        list = new Object[DEFAULT_CAPACITY];
    }

    public MyArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("ArrayList size must be greater than 0");
        } else if (capacity == 0) {
            list = EMPTY_ELEMENTDATA;
        } else {
            this.list = new Object[capacity];
        }
    }

    public void add(S obj) {
        add(obj, size);
    }

    public void add(S obj, int index) {
        if (size + 1 < list.length) {
            expand(size + 1);
        }
        if (index != size) {
            System.arraycopy(list, index, list, index + 1, size - index);
        }
        list[index] = obj;
        size++;
    }

    public void remove(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        size--;
        if (size > index) {
            System.arraycopy(list, index + 1, list, index, size - index);
        }
    }

    public S get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (S) list[index];
    }

    private void expand(int expectedSize) {
        int oldCapacity = list.length;
        int targetCapacity = oldCapacity + (oldCapacity >> 1); // oldCapacity * 1.5
        int newCapacity = Math.max(targetCapacity, expectedSize);
        list = Arrays.copyOf(list, newCapacity);
    }

    public int size() {
        return size;
    }
}
