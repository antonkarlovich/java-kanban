package main;

import main.tasks.Status;
import main.manager.Manager;
import main.tasks.*;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();


        Task task1 = new Task("task1", "купить афобазол, а то с этими дедлайнами скоро кукуха поедет)", Status.NEW);
        manager.addNewTask(task1);


        Epic epicForRepair = new Epic("Epic1", "\"Сделать ремонт в зале\"");
        manager.addNewEpic(epicForRepair);

        SubTask subTask1 = new SubTask("subTask1", "ободрать старые обои", Status.NEW, epicForRepair.getId());
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "узнать, можно ли снести стену", Status.NEW, epicForRepair.getId());
        manager.addSubTask(subTask2);

        System.out.println("все сабтаски эпика " + manager.getAllSubTaskForEpic(epicForRepair.getId()));


        System.out.println("---------------------------------------------------------------------------------------");

        System.out.println(epicForRepair.getStatus());
        System.out.println(epicForRepair.getId());


        System.out.println("Вывожу задачу по id");
        System.out.println(manager.getTaskById(1));


        SubTask newSubTask1 = new SubTask("подзадача 1", "обновленная задача", Status.IN_PROGRESS, epicForRepair.getId());
        manager.updateSubTask(newSubTask1);

        System.out.println("---------------------------------------------------------------------------------------");

        System.out.println("Выводим все сабтаски: " + manager.getAllSubtasks());


        System.out.println(epicForRepair.getSubTaskIds());
    }
}
