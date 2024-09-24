package data;

import util.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskId;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
        subtaskId = new ArrayList<>();
    }

    public Epic(String title, String description, Status status) {
        super(title, description, status);
        subtaskId = new ArrayList<>();
        taskType = TaskType.EPIC;
    }

    public Epic(int id, String title, Status status, String description) {
        super(id, title, status, description);
        subtaskId = new ArrayList<>();
    }

    public Epic(int id, String title, String description, Status status, Duration duration, LocalDateTime startTime, LocalDateTime endTime) {
        super(id, title, description, status, duration, startTime);
        subtaskId = new ArrayList<>();
        this.endTime = endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setSubtaskId(int id) {
        subtaskId.add(id);
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void removeSubtask(Integer id) {
        subtaskId.remove(id);
    }

    public void clearSubtask() {
        subtaskId.clear();
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}

