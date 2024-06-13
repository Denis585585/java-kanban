package service;

import java.util.Optional;

public interface EpicTaskManager<T> {
    Optional<T> findSubtaskById(int epicId, int supTaskId);
    void addSubtask(int epicId, T object);
    boolean updateSubtask(int epicId, int subTaskId, T object);
    boolean removeSubtask(int epicId, int subTaskId);
}
