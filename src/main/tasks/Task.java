package main.tasks;

import java.util.Objects;

public class Task {
    protected String title;
    protected String desc;
    protected Status status;
    protected int id;

    public Task(String title, String desc, Status status) {
        this.title = title;
        this.desc = desc;
        this.status = status;
    }

    public Task(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + "," + TasksTypes.TASK.toString() + "," + title + "," + status + "," + desc;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(desc, task.desc) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, desc, status, id);
    }
}

