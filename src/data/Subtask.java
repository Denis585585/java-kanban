package data;

import util.Status;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(int id, String title, Status status, String description, int epicId) {
        super(id, title, status, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
