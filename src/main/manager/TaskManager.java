package main.manager;

import main.tasks.Epic;
import main.tasks.SubTask;
import main.tasks.Task;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager { //исправился) я просто где то читал, что надо 2 строчки между методами,
    //поэтому стрательно нажимал на enter:D

    void addNewTask(Task task);

    int addNewEpic(Epic epic);

    void addSubTask(SubTask subTask);

    Task getTaskById(int taskId);

    Epic getEpicById(int epicId);

    SubTask getSubTaskById(int subTaskId);

    void updateTask(Task task, int taskId);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<SubTask> getAllSubtasks();

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void deleteAllEpics();

    void deleteAllSubTasks();

    void deleteAllTasks();

    void deleteEpicById(int epicId);

    void deleteTaskById(int taskId);

    void deleteSubtaskById(int subTaskId);

    ArrayList<SubTask> getAllSubTaskForEpic(int epic);

    List<Task> getHistory();

}
