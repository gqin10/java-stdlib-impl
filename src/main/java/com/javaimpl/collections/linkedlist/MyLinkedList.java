package com.javaimpl.collections.linkedlist;

import java.util.NoSuchElementException;

/**
 *
 * @param <S> class of stored object
 */
public class MyLinkedList<S> {

    private Node<S> firstNode;

    private Node<S> lastNode;

    private int size;

    public void add(S item) {
        add(item, size);
    }

    public void add(S item, int index) {
        Node<S> newNode = new Node<>(item);
        if (firstNode == null) {
            addFirst(item);
        } else if (index == 0) {
            linkFirst(newNode);
        } else if (index == size) {
            linkLast(newNode);
        } else {
            linkBefore(newNode, node(index));
            size++;
        }
    }

    public void addFirst(S item) {
        linkFirst(new Node<>(item));
    }

    public void addLast(S item) {
        linkLast(new Node<>(item));
    }

    public S get(int index) {
        Node<S> node = node(index);
        return node != null ? node.value : null;
    }

    public S remove(int index) {
        Node<S> node = node(index);
        return remove(node);
    }

    public S remove(S item) {
        Node<S> node = findNode(item);
        return remove(node);
    }

    public void set(int index, S item) {
        Node<S> node = node(index);
        if (node == null) {
            throw new NoSuchElementException("element does not exist");
        }
        node.value = item;
    }

    public boolean contains(S item) {
        Node<S> node = node(item);
        return node != null;
    }

    public void clear() {
        firstNode = null;
        lastNode = null;
        size = 0;
    }

    private S remove(Node<S> node) {
        if (node == null) {
            return null;
        }
        if (node.prev != null) {
            // handle PREV -> TO_REMOVE -> NEXT
            node.prev.next = node.next;
        } else {
            // handle <null> -> TO_REMOVE -> NEXT
            firstNode = node.next;
        }
        if (node.next != null) {
            // handle PREV -> TO_REMOVE -> NEXT
            node.next.prev = node.prev;
        } else {
            // handle PREV -> TO_REMOVE -> <null>
            lastNode = node.prev;
        }
        size--;
        return node.value;
    }

    private Node<S> findNode(S item) {
        Node<S> currNode = firstNode;
        while (currNode != null) {
            if (currNode.value.equals(item)) {
                return currNode;
            }
            currNode = currNode.next;
        }
        return null;
    }

    private void linkFirst(Node<S> newNode) {
        if (firstNode == null) {
            firstNode = lastNode = newNode;
        } else {
            newNode.next = firstNode;
            firstNode.prev = newNode;
            firstNode = newNode;
        }
        size++;
    }

    private void linkLast(Node<S> newNode) {
        if (firstNode == null) {
            firstNode = lastNode = newNode;
        } else {
            lastNode.next = newNode;
            newNode.prev = lastNode;
            lastNode = newNode;
        }
        size++;
    }

    private void linkBefore(Node<S> newNode, Node<S> nextNode) {
        // otherNode -> [newNode] -> nextNode
        if (nextNode.prev != null) {
            nextNode.prev.next = newNode;
            newNode.prev = nextNode.prev;
        }
        newNode.next = nextNode;
        nextNode.prev = newNode;
    }

    private void linkAfter(Node<S> newNode, Node<S> prevNode) {
        // prevNode -> [newNode] -> otherNode
        if (prevNode.next != null) {
            prevNode.next.prev = newNode;
            newNode.next = prevNode.next;
        }
        prevNode.next = newNode;
        newNode.prev = prevNode;
    }

    private Node<S> node(int index) {
        if (index < (size >> 1)) {
            return getFromHead(index);
        }
        return getFromTail(index);
    }

    private Node<S> node(S item) {
        Node<S> currNode = firstNode;
        while (currNode != null) {
            if (currNode.value.equals(item)) {
                return currNode;
            }
            currNode = currNode.next;
        }
        return null;
    }

    private Node<S> getFromHead(int index) {
        int i = 0;
        Node<S> currNode = firstNode;
        while (currNode != null) {
            if (index == i) {
                return currNode;
            }
            i++;
            currNode = currNode.next;
        }
        return null;
    }

    private Node<S> getFromTail(int index) {
        int i = size - 1;
        Node<S> currNode = lastNode;
        while (currNode != null) {
            if (index == i) {
                return currNode;
            }
            i--;
            currNode = currNode.prev;
        }
        return null;
    }

    public int size() {
        return size;
    }

    public static class Node<S> {
        public S value;

        public Node<S> prev;

        public Node<S> next;

        public Node(S value) {
            this.value = value;
        }
    }
}
