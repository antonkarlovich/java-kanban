package main.manager;

import main.tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private static final List<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {
        int MAX_SIZE_HISTORY = 10;
        if (historyList.size() >= MAX_SIZE_HISTORY) {
            historyList.remove(0);
        }
        historyList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
