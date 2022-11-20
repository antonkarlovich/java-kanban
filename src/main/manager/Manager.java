package main.manager;

import java.util.*;

import main.tasks.*;


public class Manager {
    private static int nextId = 1;
    protected HashMap<Integer, Task> tasksMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicsMap = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasksMap = new HashMap<>();

    public void addNewTask(Task task) {
        task.setId(nextId++);
        tasksMap.put(task.getId(), task);
    }


    public int addNewEpic(Epic epic) {
        epic.setId(nextId++);
        epicsMap.put(epic.getId(), epic);

        updateEpicStatus(epic.getId());
        return epic.getId();
    }


    public void addSubTask(SubTask subTask) {
        subTask.setId(nextId++);
        subTasksMap.put(subTask.getId(), subTask);
        epicsMap.get(subTask.epicId).addSubTaskId(subTask.getId());
        updateEpicStatus(subTask.getEpicId());
    }

    public Task getTaskById(int taskId) {
        return tasksMap.get(taskId);
    }

    public Epic getEpicById(int epicId) {
        return epicsMap.get(epicId);
    }

    public SubTask getSubTaskById(int subTaskId) {
        return subTasksMap.get(subTaskId);
    }


    public void updateTask(Task task, int taskId) {
        if (tasksMap.containsKey(taskId)) {
            tasksMap.put(taskId, task);
        }
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }


    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicsMap.values());
    }


    public ArrayList<SubTask> getAllSubtasks() {
        return new ArrayList<>(subTasksMap.values());
    }


    public void updateEpic(Epic epic) {
        if (epicsMap.containsKey(epic.getId())) {
            epicsMap.put(epic.getId(), epic);
        }
        updateEpicStatus(epic.getId());
    }

    public void updateSubTask(SubTask subTask) {
        int epicId = 0;
        if (subTasksMap.containsKey(subTask.getId())) {
            epicId = subTasksMap.get(subTask.getId()).getEpicId();
            subTasksMap.put(subTask.getId(), subTask);
            updateEpicStatus(epicId);
        }
    }


    public void deleteAllEpics() {
        epicsMap.clear();
        subTasksMap.clear();
    }

    public void deleteAllSubTasks() {
        for (Epic epic : epicsMap.values()) {
            epic.getSubTaskIds().clear();
        }
        subTasksMap.clear();
    }

    public void deleteAllTasks() {
        tasksMap.clear();
    }

    public void deleteEpicById(int epicId) {
        ArrayList<Integer> subTaskIds = epicsMap.get(epicId).getSubTaskIds();
        epicsMap.remove(epicId);

        for (Integer subId : subTaskIds) {
            subTasksMap.remove(subId);
        }
    }


    public void  deleteTaskById(int taskId) {
        tasksMap.remove(taskId);
    }

    public void deleteSubtaskById(int subTaskId) {
        int epicId = subTasksMap.get(subTaskId).getEpicId();
        subTasksMap.remove(subTaskId);
        updateEpicStatus(epicId);
    }



    public ArrayList<SubTask> getAllSubTaskForEpic(int epic) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for (int subTask : subTasksMap.keySet()) {
            if (subTasksMap.get(subTask).epicId == epic) {
                subTasks.add(subTasksMap.get(subTask));
            }
        }
        return subTasks;
    }

    //да, он был приватным)) он работал как то странно, поэтому я его пробовал тестить в манагере,
    // а потом забыл поменять модификатор доступа
    private void updateEpicStatus(int epicId) {
        int statusNew = 0;
        int statusDone = 0;
        Status value;
        if (epicsMap.get(epicId).getSubTaskIds().isEmpty()) {
            epicsMap.get(epicId).setStatus(Status.NEW);
        } else {
            for (int sub : epicsMap.get(epicId).getSubTaskIds()) {
                value = subTasksMap.get(sub).getStatus();
                if (value == Status.NEW) {
                    statusNew++;
                }
                if (value == Status.DONE) {
                    statusDone++;
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
}
