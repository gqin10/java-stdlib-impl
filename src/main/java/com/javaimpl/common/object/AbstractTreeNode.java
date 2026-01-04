package com.javaimpl.common.object;

import com.javaimpl.collections.linkedhashmap.MyLinkedHashMap;

/**
 * extends MyLinkedHashMap.LinkedHashMapNode so that TreeNode can be used for regular HashMap and LinkedHashMap
 *
 * @param <S> class of key
 * @param <T> class of value
 * @param <U> concrete class that extends AbstractTreeNode
 */
public abstract class AbstractTreeNode<S, T, U extends AbstractTreeNode<S, T, U>> extends MyLinkedHashMap.LinkedHashMapNode<S, T> {

    public U left;

    public U right;

    public AbstractTreeNode(S key, T value) {
        super(key, value);
    }

    public void put(S key, T value) {
        put((U) this, key, value);
    }

    private void put(U head, S key, T value) {
        if (key.equals(head.key)) {
            head.value = value;
        }
        if (key.hashCode() < head.key.hashCode()) {
            if (head.left != null) {
                put(head.left, key, value);
            } else {
                head.left = createNode(key, value);
            }
        } else if (key.hashCode() > head.key.hashCode()) {
            if (head.right != null) {
                put(head.right, key, value);
            } else {
                head.right = createNode(key, value);
            }
        }
        // same hashcode?
    }

    protected abstract U createNode(S key, T value);

}
