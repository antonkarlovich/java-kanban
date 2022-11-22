package main.manager;
import java.util.*;
import main.tasks.*;

public class InMemoryTaskManager implements TaskManager{
    private static int nextId = 1;
    private final HashMap<Integer, Task> tasksMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicsMap = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasksMap = new HashMap<>();
    private final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();


    @Override
    public void addNewTask(Task task) {
        task.setId(nextId++);
        tasksMap.put(task.getId(), task);
    }

    @Override
    public int addNewEpic(Epic epic) {
        epic.setId(nextId++);
        epicsMap.put(epic.getId(), epic);

        updateEpicStatus(epic.getId());
        return epic.getId();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        subTask.setId(nextId++);
        subTasksMap.put(subTask.getId(), subTask);
        epicsMap.get(subTask.epicId).addSubTaskId(subTask.getId());
        updateEpicStatus(subTask.getEpicId());
    }

    @Override
    public Task getTaskById(int taskId) {
        historyManager.add(tasksMap.get(taskId));
        return tasksMap.get(taskId);
    }


    @Override
    public Epic getEpicById(int epicId) {
        historyManager.add(epicsMap.get(epicId));
        return epicsMap.get(epicId);
    }


    @Override
    public SubTask getSubTaskById(int subTaskId) {
        historyManager.add(subTasksMap.get(subTaskId));
        return subTasksMap.get(subTaskId);
    }


    @Override
    public void updateTask(Task task, int taskId) {
        if (tasksMap.containsKey(taskId)) {
            tasksMap.put(taskId, task);
        }
    }


    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }



    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicsMap.values());
    }



    @Override
    public ArrayList<SubTask> getAllSubtasks() {
        return new ArrayList<>(subTasksMap.values());
    }



    @Override
    public void updateEpic(Epic epic) {
        if (epicsMap.containsKey(epic.getId())) {
            epicsMap.put(epic.getId(), epic);
        }
        updateEpicStatus(epic.getId());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        int epicId = 0;
        if (subTasksMap.containsKey(subTask.getId())) {
            epicId = subTasksMap.get(subTask.getId()).getEpicId();
            subTasksMap.put(subTask.getId(), subTask);
            updateEpicStatus(epicId);
        }
    }


    @Override
    public void deleteAllEpics() {
        epicsMap.clear();
        subTasksMap.clear();
    }


    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epicsMap.values()) {
            epic.getSubTaskIds().clear();
            epic.setStatus(Status.NEW);
        }
        subTasksMap.clear();
    }


    @Override
    public void deleteAllTasks() {
        tasksMap.clear();
    }


    @Override
    public void deleteEpicById(int epicId) {
        ArrayList<Integer> subTaskIds = epicsMap.get(epicId).getSubTaskIds();
        epicsMap.remove(epicId);

        for (Integer subId : subTaskIds) {
            subTasksMap.remove(subId);
        }
    }


    @Override
    public void  deleteTaskById(int taskId) {
        tasksMap.remove(taskId);
    }


    @Override
    public void deleteSubtaskById(int subTaskId) {
        int epicId = subTasksMap.get(subTaskId).getEpicId();
        subTasksMap.remove(subTaskId);
        updateEpicStatus(epicId);
    }

    @Override
    public ArrayList<SubTask> getAllSubTaskForEpic(int epic) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (int subTask : subTasksMap.keySet()) {
            if (subTasksMap.get(subTask).epicId == epic) {
                subTasks.add(subTasksMap.get(subTask));
            }
        }
        return subTasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    private void updateEpicStatus(int epicId) {
        int statusNew = 0;
        int statusDone = 0;

        if (epicsMap.get(epicId).getSubTaskIds().isEmpty()) {
            epicsMap.get(epicId).setStatus(Status.NEW);
        } else {
            for (int sub : epicsMap.get(epicId).getSubTaskIds()) {
                ;
                if (subTasksMap.get(sub).getStatus() == Status.NEW) {
                    statusNew++;
                }
                if (subTasksMap.get(sub).getStatus() == Status.DONE) {
                    statusDone++;
                }
            }
            if (statusNew == epicsMap.get(epicId).getSubTaskIds().size()) {
                epicsMap.get(epicId).setStatus(Status.NEW);
            } else if (statusDone == epicsMap.get(epicId).getSubTaskIds().size()) {
                epicsMap.get(epicId).setStatus(Status.DONE);
            } else {
                epicsMap.get(epicId).setStatus(Status.IN_PROGRESS);
            }
        }
    }
}
