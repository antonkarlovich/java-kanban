package main.manager;

import main.tasks.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> historyList = new ArrayList<>();
    private int MAX_SIZE_HISTORY = 10;//а его просто перенес из метода видимо)) поэтому и забыл про модификатор

    @Override
    public void add(Task task) {
        if (task == null)
            return;

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
