package ua.sopsany.utils;

import java.util.*;

public class GenericRepository<T> {
    private List<T> items = new  ArrayList<T>();

    public void add(T item) {
        items.add(item);
    }

    public void remove(T item) {
        items.remove(item);
    }

    public List<T> getAll() {
        return items;
    }

}
