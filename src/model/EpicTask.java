package model;

import util.TaskStatusEnum;

import java.util.HashMap;
import java.util.Map;

public class EpicTask extends Task{
    private Map<Integer,Subtask> subtasks;

    public EpicTask(String title, String description, TaskStatusEnum status, Map<Integer, Subtask> subtasks) {
        super(title, description, status);
        this.subtasks = subtasks;
    }

    public EpicTask(String title, String description, TaskStatusEnum status) {
        super(title, description, status);
        subtasks = new HashMap<>();
    }

    public Map<Integer,Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Map<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status + '\'' +
                ", subtasks=" + subtasks.values() +
                '}';
    }
}
