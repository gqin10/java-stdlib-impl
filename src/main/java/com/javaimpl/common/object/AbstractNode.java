package com.javaimpl.common.object;

/**
 *
 * @param <S> class of value
 */
public abstract class AbstractNode<S, T> {

    public S key;

    public T value;

    public int hash;

    public AbstractNode(S key, T value) {
        this.key = key;
        this.value = value;
        this.hash = key.hashCode();
    }
}
