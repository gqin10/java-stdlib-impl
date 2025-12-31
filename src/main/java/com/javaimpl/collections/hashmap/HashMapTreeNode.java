package com.javaimpl.collections.hashmap;

import com.javaimpl.common.object.AbstractTreeNode;

import java.util.Objects;

public class HashMapTreeNode<S, T> extends AbstractTreeNode<S, T, HashMapTreeNode<S, T>> {

    public HashMapTreeNode<S, T> parent;

    public HashMapTreeNode<S, T> prev;

    public HashMapTreeNode<S, T> next;

    public boolean isRed;

    public HashMapTreeNode(HashMapTreeNode<S, T> parent, S key, T value) {
        super(key, value);
        this.parent = parent;
    }

    public HashMapTreeNode(S key, T value) {
        super(key, value);
    }

    private HashMapTreeNode<S, T> rotateLeft(HashMapTreeNode<S, T> node) {
        HashMapTreeNode<S, T> right, parent, rightLeft;
        if (node != null && (right = node.right) != null) {
            if ((rightLeft = node.right = right.left) != null)
                rightLeft.parent = node;
            if ((parent = right.parent = node.parent) == null) {
                // node was root, now right is root, change to black node
                right.isRed = false;
            } else if (parent.left == node)
                parent.left = right;
            else
                parent.right = right;
            right.left = node;
            node.parent = right;
            return right;
        }
        return node;
    }

    private HashMapTreeNode<S, T> rotateRight(HashMapTreeNode<S, T> node) {
        HashMapTreeNode<S, T> left, parent, leftRight;
        if (node != null && (left = node.left) != null) {
            if ((leftRight = node.left = left.right) != null)
                leftRight.parent = node;
            if ((parent = left.parent = node.parent) == null) {
                // node was root, now left is root, change to black node
                left.isRed = false;
            } else if (parent.right == node)
                parent.right = left;
            else
                parent.left = left;
            left.right = node;
            node.parent = left;
            return left;
        }
        return node;
    }

    private void balanceTree(HashMapTreeNode<S, T> newNode) {
        newNode.isRed = true;
        for (HashMapTreeNode<S, T> parent, grandparent, grandparentLeft, grandparentRight; ; ) {
            if ((parent = newNode.parent) == null) {
                // newNode is the root
                newNode.isRed = false;
                return;
            }
            grandparent = parent.parent;
            if (!parent.isRed || grandparent == null)
                return;
            if (parent == (grandparentLeft = grandparent.left)) {
                // if parent node is on the left of grandparent
                if ((grandparentRight = grandparent.right) != null && grandparentRight.isRed) { // Case 1: Uncle is Red
                    /**
                     * 1. change parent and uncle to BLACK
                     * 2. change grandparent to RED
                     * 3. treat grandparent as newNode and proceed with next loop to check for any violation of red-black tree rules
                     */
                    grandparentRight.isRed = false;
                    parent.isRed = false;
                    grandparent.isRed = true;
                    newNode = grandparent;
                } else { // Case 2 & 3: Uncle is Black
                    if (newNode == parent.right) { // Case 2: Inner Child (Triangle) -> Rotate to Line
                        rotateLeft(parent); //
                        parent = newNode;
                        grandparent = parent.parent;
                    }
                    if (parent != null) { // Case 3: Outer Child (Line) -> Rotate + Recolor
                        parent.isRed = false;
                        if (grandparent != null) {
                            grandparent.isRed = true;
                            rotateRight(grandparent);
                        }
                    }
                }
            } else { // Symmetric to above
                // if parent node is on the right of grandparent
                if (grandparentLeft != null && grandparentLeft.isRed) {
                    grandparentLeft.isRed = false;
                    parent.isRed = false;
                    grandparent.isRed = true;
                    newNode = grandparent;
                } else {
                    if (newNode == parent.left) {
                        rotateRight(parent);
                        parent = newNode;
                        grandparent = parent.parent;
                    }
                    if (parent != null) {
                        parent.isRed = false;
                        if (grandparent != null) {
                            grandparent.isRed = true;
                            rotateLeft(grandparent);
                        }
                    }
                }
            }
        }
    }

    public HashMapTreeNode<S, T> getRoot() {
        HashMapTreeNode<S, T> root = this;
        while (root.parent != null)
            root = root.parent;
        return root;
    }

    @Override
    public void put(S key, T value) {
        int hash = key == null ? 0 : key.hashCode();
        HashMapTreeNode<S, T> root = getRoot(), current = root;
        HashMapTreeNode<S, T> parent = null;

        if (current == null) {
            // Should usually not happen if 'this' is a valid node unless tree is empty but we are in instance.
            // If tree is empty, usage of this method is suspect without 'this' being the first node.
        }

        do {
            parent = current;
            if (current.hash == hash && Objects.equals(key, current.key)) {
                // existing key found, update value
                current.value = value;
                return;
            } else if (current.hash <= hash) {
                current = current.right;
            } else {
                current = current.left;
            }
        } while (current != null);

        HashMapTreeNode<S, T> newNode = new HashMapTreeNode<>(key, value);
        newNode.parent = parent;
        if (parent.hash > hash)
            parent.left = newNode;
        else
            parent.right = newNode;

        // Maintain Linked List (insert after parent)
        newNode.next = parent.next;
        if (parent.next != null) parent.next.prev = newNode;
        parent.next = newNode;
        newNode.prev = parent;

        balanceTree(newNode);
    }

    public HashMapTreeNode<S, T> treeify() {
        HashMapTreeNode<S, T> currNode = this.next;
        this.isRed = false; // root node should be black

        while (currNode != null) {
            currNode.isRed = true;

            // Find current root
            HashMapTreeNode<S, T> traverseNode = getRoot();

            while (true) {
                // traverse whole tree until found correct position to link node
                if (currNode.hash < traverseNode.hash) {
                    if (traverseNode.left != null) {
                        // find position in next round
                        traverseNode = traverseNode.left;
                    } else {
                        // position found, link node
                        currNode.parent = traverseNode;
                        traverseNode.left = currNode;
                        balanceTree(currNode);
                        break;
                    }
                } else {
                    if (traverseNode.right != null) {
                        // find position in next round
                        traverseNode = traverseNode.right;
                    } else {
                        // position found, link node
                        currNode.parent = traverseNode;
                        traverseNode.right = currNode;
                        balanceTree(currNode);
                        break;
                    }
                }
            }
            // handle currNode become tree root after rotation
            if (currNode.parent == null) {
                currNode.isRed = false;
            }
            currNode = currNode.next;
        }

        // Return the root
        HashMapTreeNode<S, T> root = this;
        while (root.parent != null) {
            root = root.parent;
        }
        return root;
    }

    @Override
    protected HashMapTreeNode<S, T> createNode(S key, T value) {
        return new HashMapTreeNode<>(key, value);
    }

    public int size() {
        return 1 + (this.left == null ? 0 : this.left.size()) + (this.right == null ? 0 : this.right.size());
    }

    public void printTree() {
        printTree("", true);
    }

    private void printTree(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") +
                String.format("key=%s, val=%s, color=%s", key, value, isRed ? "RED" : "BLACK"));
        String childPrefix = prefix + (isTail ? "    " : "│   ");
        if (this.left != null) {
            this.left.printTree(childPrefix, this.right == null);
        }
        if (this.right != null) {
            this.right.printTree(childPrefix, true);
        }
    }
}