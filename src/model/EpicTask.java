package model;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task{
    private final List<Task> subtasks;

    public EpicTask() {
        subtasks = new ArrayList<>();
    }

    public List<Task> getSubtasks() {
        return subtasks;
    }
}
