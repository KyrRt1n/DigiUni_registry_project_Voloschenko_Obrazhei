package ua.sopsany.utils;

import ua.sopsany.exceptions.*;

import java.util.*;
import java.util.function.Predicate;

public class GenericRepository<T> {
    private List<T> items = new ArrayList<T>();

    public void add(T item) throws DuplicateIdException {
        if (items.contains(item)) {
            throw new DuplicateIdException("Item already exists in repository: " + item);
        }
        items.add(item);
    }

    public void remove(T item) throws EntityNotFoundException {
        if (!items.remove(item)) {
            throw new EntityNotFoundException("Item not found in repository: " + item);
        }
    }

    public List<T> getAll() {
        return items;
    }

    public Optional<T> findBy(Predicate<T> predicate) {
        return items.stream().filter(predicate).findFirst();
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}