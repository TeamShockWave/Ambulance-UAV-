package com.firebase.client.collection;

import com.firebase.client.collection.ImmutableSortedMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ImmutableSortedSet<T> implements Iterable<T> {
    private final ImmutableSortedMap<T, Void> map;

    private static class WrappedEntryIterator<T> implements Iterator<T> {
        final Iterator<Map.Entry<T, Void>> iterator;

        public WrappedEntryIterator(Iterator<Map.Entry<T, Void>> iterator2) {
            this.iterator = iterator2;
        }

        public boolean hasNext() {
            return this.iterator.hasNext();
        }

        public T next() {
            return this.iterator.next().getKey();
        }

        public void remove() {
            this.iterator.remove();
        }
    }

    public ImmutableSortedSet(List<T> elems, Comparator<T> comparator) {
        this.map = ImmutableSortedMap.Builder.buildFrom(elems, Collections.emptyMap(), ImmutableSortedMap.Builder.identityTranslator(), comparator);
    }

    private ImmutableSortedSet(ImmutableSortedMap<T, Void> map2) {
        this.map = map2;
    }

    public boolean contains(T entry) {
        return this.map.containsKey(entry);
    }

    public ImmutableSortedSet<T> remove(T entry) {
        ImmutableSortedMap<T, Void> newMap = this.map.remove(entry);
        return newMap == this.map ? this : new ImmutableSortedSet<>(newMap);
    }

    public ImmutableSortedSet<T> insert(T entry) {
        return new ImmutableSortedSet<>(this.map.insert(entry, null));
    }

    public T getMinEntry() {
        return this.map.getMinKey();
    }

    public T getMaxEntry() {
        return this.map.getMaxKey();
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public Iterator<T> iterator() {
        return new WrappedEntryIterator(this.map.iterator());
    }

    public Iterator<T> iteratorFrom(T entry) {
        return new WrappedEntryIterator(this.map.iteratorFrom(entry));
    }

    public Iterator<T> reverseIteratorFrom(T entry) {
        return new WrappedEntryIterator(this.map.reverseIteratorFrom(entry));
    }

    public Iterator<T> reverseIterator() {
        return new WrappedEntryIterator(this.map.reverseIterator());
    }

    public T getPredecessorEntry(T entry) {
        return this.map.getPredecessorKey(entry);
    }
}
