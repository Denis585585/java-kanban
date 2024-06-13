package model;

import util.TaskStatusEnum;

public class Subtask extends Task{
    public Subtask(String title, String description, TaskStatusEnum status) {
        super(title, description, status);
    }
}
