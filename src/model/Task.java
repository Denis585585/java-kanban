package model;

import util.TaskStatusEnum;

import java.util.Objects;

public class Task {
    protected int id = 0;
    private String title;
    private String description;
    private TaskStatusEnum status;
    private static int counter = 0;

    static {
        counter++;
    }

    public Task(){
        this.id = counter;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return this.id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatusEnum getStatus() {
        return status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatusEnum status) {
        this.status = status;
    }
}
