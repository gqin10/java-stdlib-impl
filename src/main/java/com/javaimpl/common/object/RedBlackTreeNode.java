package com.javaimpl.common.object;

/**
 *
 * @param <S> class of key
 * @param <T> class of value
 */
public class RedBlackTreeNode<S, T> {

    public RedBlackTreeNode<S, T> parent;

    public RedBlackTreeNode<S, T> left;

    public RedBlackTreeNode<S, T> right;

    public S key;

    public T value;

    public boolean isRed = true;

    public RedBlackTreeNode(S key, T value) {
        this.key = key;
        this.value = value;
    }

    public RedBlackTreeNode<S, T> getRoot() {
        if (parent != null) {
            return this.parent.getRoot();
        }
        return this;
    }

}
