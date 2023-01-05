package main.manager;
import java.util.*;
import main.tasks.*;

public class InMemoryTaskManager implements TaskManager{
    protected int nextId = 1;
    protected final HashMap<Integer, Task> tasksMap = new HashMap<>();
    protected final HashMap<Integer, Epic> epicsMap = new HashMap<>();
    protected final HashMap<Integer, Subtask> subTasksMap = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();


    @Override
    public void addNewTask(Task task) {
        if (task.getId() == 0)
            task.setId(getNextId());
        tasksMap.put(task.getId(), task);
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        if (subtask.getId() == 0)
            subtask.setId(getNextId());
        subTasksMap.put(subtask.getId(), subtask);
    }

    @Override
    public void addNewEpic(Epic epic) {
        if (epic.getId() == 0) {
            epic.setId(getNextId());
        }
        epicsMap.put(epic.getId(), epic);
        for (Integer subtaskId : epic.getSubtasksId()) {
            subTasksMap.get(subtaskId).setEpicID(epic.getId());
        }
        setEpicStatus(epic);
    }

    private void setEpicStatus(Epic epic) {
        int counter = 0;
        int min = 0;
        int max = 0;
        for (Integer subtaskId : epic.getSubtasksId()) {
            min++;
            max += 3;
            if (subTasksMap.get(subtaskId).getStatus().equals(Status.NEW)){
                counter++;
            } else if (subTasksMap.get(subtaskId).getStatus().equals(Status.DONE)) {
                counter += 3;
            } else {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
        }
        if (counter == min) {
            epic.setStatus(Status.NEW);
        } else if (counter == max) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    private int getNextId(){
        return nextId++;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subTasksMap.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epicsMap.values());
    }

    @Override
    public void removeAllTasks() {
        tasksMap.clear();
    }

    @Override
    public void removeAllSubtasks() {
        subTasksMap.clear();
    }

    @Override
    public void removeAllEpics() {
        epicsMap.clear();
    }

    @Override
    public Task getTask(int id) {
        Task task = tasksMap.getOrDefault(id, null);
        if (task != null)
            addTaskToHistory(task);
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask task = subTasksMap.getOrDefault(id, null);
        if (task != null)
            addTaskToHistory(task);
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic task = epicsMap.getOrDefault(id, null);
        if (task != null)
            addTaskToHistory(task);
        return task;
    }

    @Override
    public void updateTask(Task newTask) {
        tasksMap.put(newTask.getId(), newTask);
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        subTasksMap.put(newSubtask.getId(), newSubtask);
    }

    @Override
    public void updateEpic(Epic newEpic) {
        int epicId = newEpic.getId();
        epicsMap.remove(epicId);
        addNewEpic(newEpic);
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        tasksMap.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        historyManager.remove(id);
        subTasksMap.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epicToDel = epicsMap.get(id);
        for (Integer subtaskId : epicToDel.getSubtasksId()) {
            historyManager.remove(subtaskId);
        }
        historyManager.remove(id);
        epicsMap.remove(id);
    }

    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtasksId()) {
            subtaskList.add(subTasksMap.get(subtaskId));
        }
        return subtaskList;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected void addTaskToHistory(Task task) {
        if (task != null) {
            historyManager.add(task);
        }
    }
}

