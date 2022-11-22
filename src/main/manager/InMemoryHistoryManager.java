package main.manager;

import main.tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> historyList = new ArrayList<>();
    int MAX_SIZE_HISTORY = 10;

    @Override
    public void add(Task task) {
        if (task != null) {
            if (historyList.size() >= MAX_SIZE_HISTORY) {
                historyList.remove(0);
            }
            historyList.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
