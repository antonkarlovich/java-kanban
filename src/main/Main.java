package main;

import main.manager.FileBackedTasksManager;
import main.manager.Managers;
import main.manager.TaskManager;
import main.tasks.Status;
import main.tasks.*;
import java.io.File;


public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("task1", "купить афобазол", Status.NEW);
        taskManager.addNewTask(task1);


        Subtask subTask1 = new Subtask("subTask1", "ободрать старые обои", Status.NEW);
        taskManager.addNewSubtask(subTask1);

        Subtask subTask2 = new Subtask("subTask2", "subTask2", Status.NEW);
        taskManager.addNewSubtask(subTask2);

        Epic epicForRepair = new Epic("Epic1", "\"Сделать ремонт в зале\"");
        epicForRepair.addSubtasksId(subTask1.getId());
        epicForRepair.addSubtasksId(subTask2.getId());
        taskManager.addNewEpic(epicForRepair);

        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);
        taskManager.getEpic(4);

        System.out.println("Выводим историю просмотров:");
        System.out.println(taskManager.getHistory());

        TaskManager taskManager1 = FileBackedTasksManager.loadFromFile(new File("src/files/files.csv"));
        System.out.println("получаем задачи из файла" + taskManager1.getHistory());


    }
}
