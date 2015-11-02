package com.bandonleon.musetta.util;

import java.util.ArrayDeque;

/**
 * Created by dom on 11/1/15.
 */
public class Stack<T> extends ArrayDeque<T> {
    public void push(T item) {
        super.addFirst(item);
    }

    public T pop() {
        return super.removeFirst();
    }

    public T peek() {
        return super.peekFirst();
    }
}

