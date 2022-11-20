package main.tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    protected ArrayList<Integer> subTaskIds = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description);
    }


    public ArrayList<Integer> getSubTaskIds() {
        return subTaskIds;
    }


    public void addSubTaskId(int subId) {
        subTaskIds.add(subId);
    }


    @Override
    public String toString() {
        return "Epic{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + super.getId() +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTaskIds, epic.subTaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subTaskIds);
    }
}
