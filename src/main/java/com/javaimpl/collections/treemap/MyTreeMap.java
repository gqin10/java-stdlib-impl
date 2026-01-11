package com.javaimpl.collections.treemap;

import com.javaimpl.common.object.RedBlackTreeNode;

import java.util.Comparator;
import java.util.function.BiFunction;

/**
 * A Red-Black Tree based implementation of the Map interface.
 *
 * @param <S> the type of keys maintained by this map
 * @param <T> the type of mapped values
 */
public class MyTreeMap<S, T> {

    private final Comparator<? super S> comparator;

    private transient RedBlackTreeNode<S, T> root;

    private transient int size = 0;

    public MyTreeMap() {
        this.comparator = Comparator.comparing(Object::hashCode);
    }

    public MyTreeMap(Comparator<? super S> comparator) {
        this.comparator = comparator;
    }

    public boolean containsKey(S key) {
        return node(key) != null;
    }

    public T get(S key) {
        RedBlackTreeNode<S, T> node = node(key);
        return node != null ? node.value : null;
    }

    public int size() {
        return size;
    }

    public T put(S key, T value) {
        if (root == null) {
            root = new RedBlackTreeNode<>(key, value);
            root.isRed = false;
            size++;
            return null;
        }

        RedBlackTreeNode<S, T> currNode = root, prevNode = null;
        while (currNode != null) {
            int compareRes = comparator.compare(key, currNode.key);
            if (compareRes == 0 && currNode.key.equals(key)) {
                // replace value of same key
                T oldValue = currNode.value;
                currNode.value = value;
                return oldValue;
            } else if (compareRes <= 0) {
                prevNode = currNode;
                currNode = currNode.left;
            } else {
                prevNode = currNode;
                currNode = currNode.right;
            }
        }

        // add newNode to child of prevNode
        RedBlackTreeNode<S, T> newNode = new RedBlackTreeNode<>(key, value);
        newNode.parent = prevNode;
        if (comparator.compare(key, prevNode.key) <= 0) {
            prevNode.left = newNode;
        } else {
            prevNode.right = newNode;
        }

        size++;
        fixNode(newNode);

        return null;
    }

    public T remove(S key) {
        RedBlackTreeNode<S, T> node = node(key);
        if (node == null) {
            return null;
        }

        if (node.left != null) {
            // replace removed node with the largest node on its left branch
            RedBlackTreeNode<S, T> largestNode = getLargest(node.left);
            // unlink the largest node with its parent
            if (largestNode != node.left) {
                // largestNode is guaranteed to be a right child here
                largestNode.parent.right = largestNode.left;
                if (largestNode.left != null) largestNode.left.parent = largestNode.parent;
                largestNode.left = node.left;
                if (node.left != null) node.left.parent = largestNode;
            }

            largestNode.parent = node.parent;
            largestNode.right = node.right;
            if (node.right != null) node.right.parent = largestNode;
            largestNode.isRed = node.isRed;

            if (node.parent != null) {
                if (node == node.parent.left) node.parent.left = largestNode;
                else node.parent.right = largestNode;
            } else {
                root = largestNode;
            }
            root = getRoot(largestNode);
        } else if (node.right != null) {
            // replace removed node with the smallest node on its right branch
            RedBlackTreeNode<S, T> smallestNode = getSmallest(node.right);
            // unlink the smallest node with its parent
            if (smallestNode != node.right) {
                // smallestNode is guaranteed to be a left child here
                smallestNode.parent.left = smallestNode.right;
                if (smallestNode.right != null) smallestNode.right.parent = smallestNode.parent;

                smallestNode.right = node.right;
                if (node.right != null) node.right.parent = smallestNode;
            }

            smallestNode.parent = node.parent;
            smallestNode.left = node.left;
            if (node.left != null) node.left.parent = smallestNode;
            smallestNode.isRed = node.isRed;

            if (node.parent != null) {
                if (node == node.parent.left) node.parent.left = smallestNode;
                else node.parent.right = smallestNode;
            } else {
                root = smallestNode;
            }
            root = getRoot(smallestNode);
        } else if (node.parent == null) {
            // removed node is the only node
            root = null;
        } else if (node == node.parent.left) {
            // no left and right branches & is parent's right node, unlink node
            node.parent.left = null;
        } else if (node == node.parent.right) {
            // no left and right branches & is parent's right node, unlink node
            node.parent.right = null;
        }

        size--;
        return node.value;
    }

    protected RedBlackTreeNode<S, T> getLargest(RedBlackTreeNode<S, T> node) {
        RedBlackTreeNode<S, T> currNode = node, prevNode = null;
        while (currNode != null) {
            prevNode = currNode;
            currNode = currNode.right;
        }
        return prevNode != null ? prevNode : node;
    }

    protected RedBlackTreeNode<S, T> getSmallest(RedBlackTreeNode<S, T> node) {
        RedBlackTreeNode<S, T> currNode = node, prevNode = null;
        while (currNode != null) {
            prevNode = currNode;
            currNode = currNode.left;
        }
        return prevNode;
    }

    protected void fixNode(RedBlackTreeNode<S, T> newNode) {
        if (newNode.parent == null) {
            newNode.isRed = false;
            return;
        }
        if (!newNode.isRed || !newNode.parent.isRed) {
            // no fix required
            return;
        }

        RedBlackTreeNode<S, T> parent = newNode.parent,
                grandParent = parent.parent,
                grandParentLeft = grandParent != null ? grandParent.left : null,
                grandParentRight = grandParent != null ? grandParent.right : null;

        if (parent == grandParentLeft) {
            if (grandParentRight != null && grandParentRight.isRed) {
                grandParentRight.isRed = grandParentLeft.isRed = false;
                grandParent.isRed = true;
                fixNode(grandParent);
                return;
            }

            // if parent is left child of grandparent
            if (newNode == parent.left) {
                // LL
                rotateRight(parent);
            } else {
                // LR
                rotateRight(rotateLeft(parent));
            }
        } else if (parent == grandParentRight) {
            if (grandParentLeft != null && grandParentLeft.isRed) {
                grandParentRight.isRed = grandParentLeft.isRed = false;
                grandParent.isRed = true;
                fixNode(grandParent);
                return;
            }

            // if parent is right child of grandparent
            if (newNode == parent.left) {
                // RL
                rotateLeft(rotateRight(parent));
            } else {
                // RR
                rotateLeft(parent);
            }
        }
        root = getRoot();
    }

    protected RedBlackTreeNode<S, T> node(S key) {
        RedBlackTreeNode<S, T> currNode = root;
        while (currNode != null) {
            int compareRes = comparator.compare(key, currNode.key);
            if (compareRes == 0 && currNode.key.equals(key)) {
                return currNode;
            } else if (compareRes <= 0)
                currNode = currNode.left;
            else
                currNode = currNode.right;
        }
        return null;
    }

    protected RedBlackTreeNode<S, T> rotateLeft(RedBlackTreeNode<S, T> node) {
        if (node == null || node.parent == null) {
            return null;
        }

        RedBlackTreeNode<S, T> parent = node.parent, rightNode = node.right;
        parent.right = node.left;
        if (node.left != null) {
            node.left.parent = parent;
        }
        node.left = parent;
        node.parent = parent.parent;
        parent.parent = node;

        return node;
    }

    protected RedBlackTreeNode<S, T> rotateRight(RedBlackTreeNode<S, T> node) {
        if (node == null || node.parent == null) {
            return null;
        }

        RedBlackTreeNode<S, T> parent = node.parent, leftNode = node.left;

        parent.left = leftNode;
        leftNode.parent = parent;
        node.right = parent;
        parent.parent = node;
        return leftNode;
    }

    protected RedBlackTreeNode<S, T> getRoot() {
        return getRoot(root);
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public S firstKey() {
        if (root == null) {
            return null;
        } else if (root.left == null && root.right == null) {
            return root.key;
        } else if (root.left != null) {
            return getSmallest(root.left).key;
        }
        return getSmallest(root.right).key;
    }

    public S lastKey() {
        if (root == null) {
            return null;
        } else if (root.left == null && root.right == null) {
            return root.key;
        } else if (root.right != null) {
            return getLargest(root.right).key;
        }
        return getLargest(root.left).key;
    }

    public S lowerKey(S key) {
        RedBlackTreeNode<S, T> node = lowerNode(key, false);
        return node != null ? node.key : null;
    }

    public S higherKey(S key) {
        RedBlackTreeNode<S, T> node = higherNode(key, false);
        return node != null ? node.key : null;
    }

    public S floorKey(S key) {
        RedBlackTreeNode<S, T> node = lowerNode(key, true);
        return node != null ? node.key : null;
    }

    public S ceilingKey(S key) {
        RedBlackTreeNode<S, T> node = higherNode(key, true);
        return node != null ? node.key : null;
    }

    protected RedBlackTreeNode<S, T> lowerNode(S key, boolean isInclusive) {
        RedBlackTreeNode<S, T> currNode = root;
        RedBlackTreeNode<S, T> result = null;
        while (currNode != null) {
            int cmp = comparator.compare(key, currNode.key);
            if (cmp > 0) {
                // key > currNode.key, so currNode is a candidate for lower
                result = currNode;
                currNode = currNode.right;
            } else if (cmp < 0) {
                // key < currNode.key, currNode is too big
                currNode = currNode.left;
            } else {
                // key == currNode.key
                if (isInclusive) {
                    return currNode;
                } else {
                    // strictly lower needed, go left
                    currNode = currNode.left;
                }
            }
        }
        return result;
    }

    protected RedBlackTreeNode<S, T> higherNode(S key, boolean isInclusive) {
        RedBlackTreeNode<S, T> currNode = root;
        RedBlackTreeNode<S, T> result = null;
        while (currNode != null) {
            int cmp = comparator.compare(key, currNode.key);
            if (cmp < 0) {
                // key < currNode.key, currNode is a candidate for higher
                result = currNode;
                currNode = currNode.left;
            } else if (cmp > 0) {
                // key > currNode.key, currNode is too small
                currNode = currNode.right;
            } else {
                // key == currNode.key
                if (isInclusive) {
                    return currNode;
                } else {
                    // strictly higher needed, go right
                    currNode = currNode.right;
                }
            }
        }
        return result;
    }

    protected RedBlackTreeNode<S, T> getRoot(RedBlackTreeNode<S, T> node) {
        RedBlackTreeNode<S, T> currNode = node, prevNode = null;
        while (currNode != null) {
            prevNode = currNode;
            currNode = currNode.parent;
        }
        return prevNode;
    }
}
