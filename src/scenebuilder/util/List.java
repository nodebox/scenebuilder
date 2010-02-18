package scenebuilder.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class List<T> implements Iterable {

    private final ArrayList<T> elements;

    public List() {
        elements = new ArrayList<T>(0);
    }

    public List(T... items) {
        elements = new ArrayList<T>(items.length);
        elements.addAll(Arrays.asList(items));
    }

    public List(Collection<T> items) {
        elements = new ArrayList<T>(items.size());
        elements.addAll(items);
    }

    public List<T> add(T item) {
        ArrayList<T> newElements = new ArrayList<T>(elements.size() + 1);
        newElements.addAll(elements);
        newElements.add(item);
        return new List<T>(newElements);
    }

    public Iterator<T> iterator() {
        return elements.iterator();
    }


}
