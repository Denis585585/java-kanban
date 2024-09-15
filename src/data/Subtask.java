package data;

import util.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, Status status, int epicId) {
        super(title, description, status);
        this.epicId = epicId;
        taskType = TaskType.SUBTASK;
    }

    public Subtask(int id, String title, Status status, String description, int epicId) {
        super(id, title, status, description);
        this.epicId = epicId;
    }

    public Subtask(int id, String title, String description, Status status,
                   Duration duration, LocalDateTime startTime, int epicId) {
        super(id, title, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }
}
