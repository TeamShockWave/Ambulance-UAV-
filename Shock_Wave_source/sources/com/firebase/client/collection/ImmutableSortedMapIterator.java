package com.firebase.client.collection;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

public class ImmutableSortedMapIterator<K, V> implements Iterator<Map.Entry<K, V>> {
    private final boolean isReverse;
    private final Stack<LLRBValueNode<K, V>> nodeStack = new Stack<>();

    ImmutableSortedMapIterator(LLRBNode<K, V> root, K startKey, Comparator<K> comparator, boolean isReverse2) {
        int cmp;
        this.isReverse = isReverse2;
        LLRBNode<K, V> node = root;
        while (!node.isEmpty()) {
            if (startKey != null) {
                cmp = isReverse2 ? comparator.compare(startKey, node.getKey()) : comparator.compare(node.getKey(), startKey);
            } else {
                cmp = 1;
            }
            if (cmp < 0) {
                if (isReverse2) {
                    node = node.getLeft();
                } else {
                    node = node.getRight();
                }
            } else if (cmp == 0) {
                this.nodeStack.push((LLRBValueNode) node);
                return;
            } else {
                this.nodeStack.push((LLRBValueNode) node);
                if (isReverse2) {
                    node = node.getRight();
                } else {
                    node = node.getLeft();
                }
            }
        }
    }

    public boolean hasNext() {
        return this.nodeStack.size() > 0;
    }

    public Map.Entry<K, V> next() {
        try {
            LLRBValueNode<K, V> node = this.nodeStack.pop();
            Map.Entry<K, V> entry = new AbstractMap.SimpleEntry<>(node.getKey(), node.getValue());
            if (this.isReverse) {
                for (LLRBNode<K, V> next = node.getLeft(); !next.isEmpty(); next = next.getRight()) {
                    this.nodeStack.push((LLRBValueNode) next);
                }
            } else {
                for (LLRBNode<K, V> next2 = node.getRight(); !next2.isEmpty(); next2 = next2.getLeft()) {
                    this.nodeStack.push((LLRBValueNode) next2);
                }
            }
            return entry;
        } catch (EmptyStackException e) {
            throw new NoSuchElementException();
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("remove called on immutable collection");
    }
}
