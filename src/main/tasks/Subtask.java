package main.tasks;


import java.util.Objects;

public class Subtask extends Task {
    private Integer epicID;

    public Subtask(String title, String desc, Status status) {
        super(title, desc, status);
    }

    public Subtask(String title, String desc, Status status, Integer epicID) {
        super(title, desc, status);
        this.epicID = epicID;
    }

    public Integer getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return id + "," + TasksTypes.SUBTASK.toString() + "," + title + "," + status + "," + desc + "," + epicID;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Subtask subtask = (Subtask) obj;
        return epicID == subtask.epicID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicID);
    }
}
