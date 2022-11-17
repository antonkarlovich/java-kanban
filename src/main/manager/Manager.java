package main.manager;

import java.util.*;
import main.status.Status;
import main.tasks.*;


public class Manager {
    private static int nextId = 1;
    protected HashMap<Integer, Task> tasksMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicsMap = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasksMap = new HashMap<>();

    public int addNewTask(Task task) {
        task.setId(nextId++);
        tasksMap.put(task.getId(), task);
        return task.getId();
    }

    public int addNewEpic(Epic epic) {
        epic.setId(nextId++);
        epicsMap.put(epic.getId(), epic);

        for (int itemId : epic.getSubTaskIds()) {
            subTasksMap.get(itemId).setEpicId(epic.getId());
        }
        return epic.getId();
    }

    public int addSubTask(SubTask subTask) {
        subTask.setId(nextId++);
        subTasksMap.put(subTask.getId(), subTask);
        return subTask.getId();
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
    //если честно, то не очень понял на счет метода Update. На вебинаре нам рассказывали, что мы создаем новый объект
    //и укладываем его на вместо старого на его id. Буду благодарен, если расскажете про это
    public void updateTask(Task task, int taskId) {
        if (tasksMap.containsKey(taskId)) {
            tasksMap.put(taskId, task);
        } else {
            System.out.println("Такой задачи не существует");
        }

    }

    public void updateEpic(Epic epic, int epicId) {
        if (epicsMap.containsKey(epicId)) {
            epicsMap.put(epicId, epic);
        } else {
            System.out.println("Такого эпика не существует");
        }

    }

    public void updateSubTask(SubTask subTask, int subId) {
        int epicId = 0;
        if (subTasksMap.containsKey(subId)) {
            epicId = subTasksMap.get(subId).getEpicId();
            subTasksMap.put(subId, subTask);
            updateEpicStatus(epicId);
        } else {
            System.out.println("Такого сабтаска не существует");
        }

    }


    public void deleteAllEpic() {
        epicsMap.clear();
        subTasksMap.clear();
    }

    public void deleteAllSubTask() {
        for (Epic epic : epicsMap.values()) {
            epic.getSubTaskIds().clear();
        }
        subTasksMap.clear();
    }

    public void deleteAllTask() {
        tasksMap.clear();
    }

    public void deleteEpic(int epicId) {
        epicsMap.remove(epicId);
    }

    public void deleteTask(int taskId) {

        if (epicsMap.containsKey(taskId)) {
            epicsMap.remove(taskId);
        } else if (subTasksMap.containsKey(taskId)) {
            int epicId = subTasksMap.get(taskId).getEpicId();
            subTasksMap.remove(taskId);
            updateEpicStatus(epicId);//если удаляется сабтаска, то пересчитывается статус эпика
        } else tasksMap.remove(taskId);
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


    private void updateEpicStatus(int epicId) {
        int statusNew = 0;
        int statusDone = 0;
        Status value;
        if (epicsMap.get(epicId).getSubTaskIds().isEmpty()) {
            epicsMap.get(epicId).setStatus(Status.NEW);
        } else {
            for (int sub : epicsMap.get(epicId).getSubTaskIds()) {
                value = epicsMap.get(sub).getStatus();
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
