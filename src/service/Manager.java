package service;

import java.util.Optional;

public interface Manager <T> {
    void clearAllTasks();
    Optional<T> findById(int id);
    void add(T object);
    boolean update(int id, T object);
    boolean remove(int id);
}
