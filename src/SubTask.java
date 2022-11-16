public class SubTask extends Task {
    protected int epicId;

    public SubTask(String title, String description, int id, Status status) {
        super(title, description, id, status);

    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", epicId=" + epicId +
                ", id=" + id +
                ", status=" + status +
                '}';
    }


}
