package com.javaimpl.collections.linkedhashmap;

import com.javaimpl.collections.hashmap.MyHashMap;
import com.javaimpl.common.object.AbstractNode;
import com.javaimpl.common.object.ListNode;

import java.util.ArrayList;
import java.util.List;

/**
 * LinkedHashMap implementation preserving insertion or access order.
 *
 * @param <S> class of key
 * @param <T> class of value
 */
public class MyLinkedHashMap<S, T> extends MyHashMap<S, T> {

    private LinkedHashMapNode<S, T> head;

    private LinkedHashMapNode<S, T> tail;

    private final boolean accessOrder;

    public MyLinkedHashMap() {
        super();
        accessOrder = false;
    }

    public MyLinkedHashMap(boolean accessOrder) {
        super();
        this.accessOrder = accessOrder;
    }

    @Override
    public T get(S key) {
        AbstractNode<S, T> node = getNode(key);
        if (accessOrder) {
            afterNodeAccess(node);
        }
        return node != null ? node.value : null;
    }

    /**
     * Returns keys in the order they are maintained (insertion or access).
     * This is essential for testing the LinkedHashMap properties.
     */
    public List<S> keys() {
        LinkedHashMapNode<S, T> currNode = head;
        List<S> keys = new ArrayList<>();
        while (currNode != null) {
            keys.add(currNode.key);
            currNode = currNode.after;
        }
        return keys;
    }

    @Override
    public void clear() {
        super.clear();
        this.head = null;
        this.tail = null;
    }

    @Override
    protected AbstractNode<S, T> newNode(S key, T value) {
        LinkedHashMapNode<S, T> node = new LinkedHashMapNode<>(key, value);
        if (head == null && tail == null) {
            head = tail = node;
        } else {
            tail.after = node;
            node.before = tail;
            tail = node;
        }
        return node;
    }

    @Override
    protected void afterNodeAccess(AbstractNode<S, T> n) {
        if (!accessOrder) {
            return;
        }

        // move node to tail after access
        LinkedHashMapNode<S, T> node = (LinkedHashMapNode<S, T>) n,
                prevNode = node.before,
                nextNode = node.after;
        if (tail == node) {
            // accessed node is already last node
            return;
        }
        if (prevNode != null && nextNode != null) {
            // link prevNode and nextNode
            prevNode.after = nextNode;
            nextNode.before = prevNode;

        } else if (prevNode == null && nextNode != null) {
            // accessed node is first node, set second node as first node
            head = nextNode;
            nextNode.before = null;
        }
        // move node to last
        tail.after = node;
        node.before = tail;
        node.after = null;
        tail = node;
    }

    @Override
    protected void afterNodeRemoval(AbstractNode<S, T> n) {
        // can force cast because all nodes are LinkedHashMapNode (created by newNode())
        LinkedHashMapNode<S, T> node = (LinkedHashMapNode<S, T>) n;
        LinkedHashMapNode<S, T> prevNode = node.before;
        LinkedHashMapNode<S, T> nextNode = node.after;
        if (prevNode != null && nextNode != null) {
            prevNode.after = nextNode;
            nextNode.before = prevNode;
        } else if (prevNode == null && nextNode == null) {
            // single item
            head = null;
            tail = null;
        } else if (prevNode == null) {
            nextNode.before = null;
            head = nextNode;
        } else {
            prevNode.after = null;
            tail = prevNode;
        }
    }

    public static class LinkedHashMapNode<S, T> extends ListNode<S, T> {

        public LinkedHashMapNode<S, T> before;

        public LinkedHashMapNode<S, T> after;

        public LinkedHashMapNode(S key, T value) {
            super(key, value);
        }
    }

}
