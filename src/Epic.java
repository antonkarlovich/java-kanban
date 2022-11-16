import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Integer> subTaskId = new ArrayList<>();

    public Epic(String title, String description, int id, Status status) {
        super(title, description, id, status);
    }


    public ArrayList<Integer> getSubTaskId() {
        return subTaskId;
    }


    public void addSubTaskId(int subId) {
        subTaskId.add(id);
    }


    @Override
    public String toString() {
        return "Epic{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }
}
