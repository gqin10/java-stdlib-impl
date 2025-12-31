package com.javaimpl.common.object;

public class ListNode<S, T> extends AbstractNode<S, T> {

    public ListNode<S, T> next;

    public ListNode(S key, T value) {
        super(key, value);
    }

    public ListNode(S key, T value, ListNode<S, T> next) {
        this(key, value);
        this.next = next;
    }

}
