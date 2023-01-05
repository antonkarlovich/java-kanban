package main.manager;

public class Managers {

    public static TaskManager getDefault() {
        return new FileBackedTasksManager("src/files/history.csv");
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
