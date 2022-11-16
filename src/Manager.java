import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    protected static int nextId = 1;
    protected HashMap<Integer, Task> tasksMap = new HashMap<>();
    protected HashMap<Integer, Epic> epicsMap = new HashMap<>();
    protected HashMap<Integer, SubTask> subTasksMap = new HashMap<>();

    public void addNewTask(Task task) {
        task.setId(nextId++);
        tasksMap.put(task.getId(), task);
    }

    public void addNewEpic(Epic epic) {
        epic.setId(nextId++);
        epicsMap.put(epic.getId(), epic);

    }

    public void addSubTask(SubTask subTask, int idEpic) {
        subTask.setId(nextId++);
        subTask.setEpicId(idEpic);
        subTasksMap.put(subTask.getId(), subTask);
        epicsMap.get(idEpic).addSubTaskId(subTask.getId());
    }

    public Task getTaskById(int taskId) {
        return tasksMap.get(taskId);
    }

    public Task getEpicById(int epicId) {
        return tasksMap.get(epicId);
    }

    public Task getSubTaskById(int subTaskId) {
        return tasksMap.get(subTaskId);
    }

    public void updateEpic(Epic epic) {
        epicsMap.put(epic.getId(), epic);
    }

    public void updateSubTask(SubTask subTask, int epicId) {
        subTask.setEpicId(epicId);
        subTasksMap.put(subTask.getId(), subTask);
        updateEpicStatus(epicId);
    }


    void updateEpicStatus(int epicId) {
        int statusNew = 0;
        int statusDone = 0;
        Status value;
        if (epicsMap.get(epicId).getSubTaskId().isEmpty()) {
            epicsMap.get(epicId).setStatus(Status.NEW);
        } else {
            for (int sub : epicsMap.get(epicId).getSubTaskId()) {
                value = epicsMap.get(sub).getStatus();
                if (value == Status.NEW) {
                    statusNew++;
                }
                if (value == Status.DONE) {
                    statusDone++;
                }
                if (statusNew == epicsMap.get(epicId).getSubTaskId().size()) {
                    epicsMap.get(epicId).setStatus(Status.NEW);
                } else if (statusDone == epicsMap.get(epicId).getSubTaskId().size()) {
                    epicsMap.get(epicId).setStatus(Status.DONE);
                } else {
                    epicsMap.get(epicId).setStatus(Status.IN_PROGRESS);
                }
            }
        }
    }

    public void removeAll() {
        deleteAllEpic();
        deleteAllSubTask();
        deleteAllTask();
    }

    public void deleteAllEpic() {
        epicsMap.clear();
    }

    public void deleteAllSubTask() {
        subTasksMap.clear();
    }

    public void deleteAllTask() {
        tasksMap.clear();
    }

    public void deleteEpic(Epic epic) {
        epicsMap.remove(epic.getId(), epic);
    }

    public void deleteSubTask(Task task) {
        int taskId = task.getId();
        int epicId = subTasksMap.get(taskId).getEpicId();

        if (epicsMap.containsKey(taskId)) {
            epicsMap.remove(taskId);
        } else if (subTasksMap.containsKey(taskId)) {
            subTasksMap.remove(taskId);
            updateEpicStatus(epicId);
        } else tasksMap.remove(taskId);


    }

    public static int getNextId() {
        nextId++;
        return nextId;
    }

    public Task getTask(int taskId) {
        return tasksMap.get(taskId);
    }

    public Task getEpic(int epicId) {
        return tasksMap.get(epicId);
    }

    public Task subTask(int subtaskId) {
        return tasksMap.get(subtaskId);
    }

    public ArrayList<Task> getAllSubTaskForEpic(int epic) {
        ArrayList<Task> subTasks = new ArrayList<>();
        for (int subTask : subTasksMap.keySet()) {
            if (subTasksMap.get(subTask).epicId == epic) {
                subTasks.add(subTasksMap.get(subTask));
            }
        }
        return subTasks;
    }


    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        for (int task : tasksMap.keySet()) {
            tasks.add(tasksMap.get(task));
        }
        for (int epic : epicsMap.keySet()) {
            tasks.add(epicsMap.get(epic));
        }
        for (int sub : subTasksMap.keySet()) {
            tasks.add(subTasksMap.get(sub));
        }
        return tasks;
    }

}
