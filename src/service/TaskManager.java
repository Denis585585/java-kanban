package service;

import java.util.Map;
import java.util.Optional;

public interface TaskManager<K, T> {
    void clearAllObjects();
    Optional<T> findById(int id);
    void add(T object);
    boolean update(int id, T object);
    boolean remove(int id);
    Map<K, T> getAll();
}
