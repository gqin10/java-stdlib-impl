package com.javaimpl.collections.hashmap;

import com.javaimpl.common.object.AbstractNode;
import com.javaimpl.common.object.ListNode;
import com.javaimpl.common.object.AbstractTreeNode;

/**
 *
 * @param <S> class of key
 * @param <T> class of value
 */
public class MyHashMap<S, T> {

    private static final int DEFAULT_LENGTH = 1 << 4; // 16

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static final int TREEIFY_THRESHOLD = 8;

    private static final int UNTREEIFY_THRESHOLD = 6;

    private static final int MINIMUM_TREEIFY_CAPACITY = 64;

    private static final int MAXIMUM_CAPACITY = 1 << 30; // maximum positive int

    private float loadFactor;

    private int threshold = 0;

    private int capacity = 0;

    private int size = 0;

    private AbstractNode<S, T>[] list;

    public MyHashMap() {
        this.capacity = DEFAULT_LENGTH;
        loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
    }

    public T put(S key, T value) {
        if (list == null) {
            resize();
        }
        int bin = getBin(key);
        // increase array capacity before put new key
        if (size >= threshold) {
            resize();
        }
        AbstractNode<S, T> head = list[bin];
        if (head != null) {
            int binCount = 0;  // to calculate number of items in this bin
            if (head instanceof HashMapTreeNode<S, T>) {
                ((HashMapTreeNode<S, T>) head).put(key, value);
            } else if (head instanceof ListNode) {
                AbstractNode<S, T> prevEntry = null;
                AbstractNode<S, T> currEntry = head;
                while (currEntry != null) {
                    binCount++;
                    if (currEntry.key.equals(key)) {
                        // update value if key exists
                        currEntry.value = value;
                        afterNodeAccess(currEntry);
                        return currEntry.value;
                    } else {
                        prevEntry = currEntry;
                        currEntry = currEntry.next;
                    }
                }
                // add new node if key does not exist
                prevEntry.next = newNode(key, value);
                if (binCount > TREEIFY_THRESHOLD - 1) {
                    treeifyBin(bin);
                }
            }
        } else {
            // set new node as head if bin is empty
            list[bin] = newNode(key, value);
        }
        size++;
        return null;
    }

    public T remove(S key) {
        int bin = getBin(key);
        AbstractNode<S, T> head = list[bin];
        if (head == null) {
            return null;
        }
        AbstractNode<S, T> prevEntry = null;
        AbstractNode<S, T> currEntry = head;
        while (currEntry != null) {
            if (currEntry instanceof ListNode) {
                if (currEntry.key.equals(key)) {
                    if (prevEntry == null) {
                        // update bin's head to second node
                        list[bin] = currEntry.next;
                    } else {
                        // link prev node to next node
                        prevEntry.next = currEntry.next;
                    }
                    size--;
                    afterNodeRemoval(currEntry);
                    return currEntry.value;
                } else {
                    prevEntry = currEntry;
                    currEntry = currEntry.next;
                }
            } else {
                // remove from tree
            }
        }
        return null;
    }

    public T get(S key) {
        AbstractNode<S, T> node = getNode(key);
        return node != null ? node.value : null;
    }

    protected AbstractNode<S, T> getNode(S key) {
        int bin = getBin(key);
        AbstractNode<S, T> currEntry = list[bin]; // get the head of bin
        while (currEntry != null) {
            if (currEntry instanceof AbstractTreeNode) {
                HashMapTreeNode<S, T> currTreeNode = (HashMapTreeNode<S, T>) currEntry;
                while (currTreeNode != null) {
                    if (currTreeNode.key.equals(key)) {
                        return currTreeNode;
                    }
                    if (hash(key) <= currTreeNode.hash) {
                        currTreeNode = currTreeNode.left;
                    } else {
                        currTreeNode = currTreeNode.right;
                    }
                }
            } else if (currEntry instanceof ListNode) {
                if (currEntry.key.equals(key)) {
                    return currEntry;
                }
                currEntry = currEntry.next;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    public void clear() {
        list = null;
        capacity = DEFAULT_LENGTH;
        size = 0;
    }

    /**
     * Applies a supplemental hash function to a given hashCode, which defends against poor quality hash functions.
     * This is critical because HashMap uses power-of-two capacity, so that otherwise only the lower bits would matter.
     * <p>
     * We shift by 16 bits because an integer is 32 bits. Shifting by half (16) ensures that the
     * "Hight Bits" (top 16) are XORed into the "Low Bits" (bottom 16), ensuring all bits contribute to the index.
     */
    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private void resize() {
        if (list == null) {
            // to initialize list
            list = new AbstractNode[capacity];
            threshold = (int) (loadFactor * capacity);
        } else if (capacity < MAXIMUM_CAPACITY) {
            int prevCapacity = capacity;
            // double up capacity
            capacity = capacity << 1;
            AbstractNode<S, T>[] prevList = list;
            list = new AbstractNode[capacity];
            threshold = (int) (loadFactor * capacity);
            threshold = capacity < MAXIMUM_CAPACITY && threshold < MAXIMUM_CAPACITY ? threshold : Integer.MAX_VALUE;
            for (int i = 0; i < prevCapacity; i++) {
                AbstractNode<S, T> currNode = prevList[i];
                if (currNode instanceof AbstractTreeNode) {
                    // TODO
                } else if (currNode instanceof ListNode<S, T>) {
                    splitAndRemap((ListNode<S, T>) currNode, capacity, i);
                }
            }
            System.out.println(String.format("Resized from %s to %s", prevCapacity, capacity));
        }
    }

    private void splitAndRemap(AbstractNode<S, T> node, int newCapacity, int binIndex) {
        AbstractNode<S, T> lowHead = null, lowTail = null, highHead = null, highTail = null;
        while (node != null) {
            if ((hash(node.key) & (newCapacity >> 1)) == 0) {
                if (lowHead == null) {
                    lowHead = node;
                }
                if (lowTail != null) {
                    lowTail.next = node;
                }
                lowTail = node;
            } else {
                if (highHead == null) {
                    highHead = node;
                }
                if (highTail != null) {
                    highTail.next = node;
                }
                highTail = node;
            }

            node = node.next;
        }
        if (lowTail != null) {
            lowTail.next = null;
            list[binIndex] = lowHead;
        }
        if (highTail != null) {
            highTail.next = null;
            list[binIndex + (newCapacity >> 1)] = highHead;
        }
    }

    private void treeifyBin(int bin) {
        if (capacity < MINIMUM_TREEIFY_CAPACITY) {
            resize();
        } else if (list[bin] != null) {
            // convert ListNode to HashMapTreeNode and link prev & next node
            HashMapTreeNode<S, T> treeRootNode = convertToTreeNodeList((ListNode<S, T>) list[bin]);
            treeRootNode.treeify();
            // update bin headNode to root of tree
            list[bin] = treeRootNode;
        }
    }

    private HashMapTreeNode<S, T> convertToTreeNodeList(ListNode<S, T> head) {
        HashMapTreeNode<S, T> root = null;
        AbstractNode<S, T> currNode = head;
        HashMapTreeNode<S, T> tailTreeNode = null;
        do {
            HashMapTreeNode<S, T> newTreeNode = new HashMapTreeNode<>(currNode.key, currNode.value);
            if (root == null) {
                root = newTreeNode;
                tailTreeNode = newTreeNode;
            } else {
                tailTreeNode.next = newTreeNode;
                newTreeNode.prev = tailTreeNode;
                tailTreeNode = newTreeNode;
            }
            currNode = currNode.next;
        } while (currNode != null);
        return root;
    }

    private int getBin(S key) {
        // Use bitwise AND to force positive index (requires capacity to be power of 2)
        return hash(key) & (capacity - 1);
    }

    protected AbstractNode<S, T> newNode(S key, T value) {
        return new ListNode<>(key, value);
    }

    protected void afterNodeInsertion(AbstractNode<S, T> node) {
    }

    protected void afterNodeAccess(AbstractNode<S, T> node) {
    }

    protected void afterNodeRemoval(AbstractNode<S, T> node) {
    }
}
