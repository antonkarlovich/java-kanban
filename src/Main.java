import main.status.Status;
import main.manager.Manager;
import main.tasks.*;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();


        Task task1 = new Task("task1", "купить афобазол, а то с этими дедлайнами скоро кукуха поедет)", Status.NEW);
        manager.addNewTask(task1);

        SubTask subTask1 = new SubTask("subTask1", "ободрать старые обои", 0, Status.NEW);
        manager.addSubTask(subTask1);
        SubTask subTask2 = new SubTask("subTask2", "узнать, можно ли снести стену", 0,Status.NEW);
        manager.addSubTask(subTask2);

        Epic epicForRepair = new Epic("Epic1", "\"Сделать ремонт в зале\"", 0, Status.NEW);

        epicForRepair.addSubTaskId(subTask1.getId());
        epicForRepair.addSubTaskId(subTask2.getId());
        manager.addNewEpic(epicForRepair);



        System.out.println("---------------------------------------------------------------------------------------");

        System.out.println(epicForRepair.getId());
        System.out.println(subTask1.getId());
        System.out.println(subTask1.epicId);
        System.out.println(subTask2.getId());
        System.out.println(subTask2.epicId);


        System.out.println("Вывожу задачу по id");
        System.out.println(manager.getTaskById(1));


        SubTask newSubTask1 = new SubTask("подзадача 1", "обновленная задача", 0,  Status.IN_PROGRESS);
        manager.updateSubTask(newSubTask1, epicForRepair.getId());

        System.out.println("---------------------------------------------------------------------------------------");


        System.out.println("---------------------------------------------------------------------------------------");

        System.out.println("Удаление сабтаски");
        manager.deleteTask(newSubTask1.getId());




    }
}
