import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();


        Task task1 = new Task("task1", "купить афобазол, а то с этими дедлайнами скоро кукуха поедет)", Status.NEW);
        manager.addNewTask(task1);

        Epic epicForRepair = new Epic("Epic1", "\"Сделать ремонт в зале\"", 0, Status.NEW);
        manager.addNewEpic(epicForRepair);
        SubTask subTask1 = new SubTask("subTask1", "ободрать старые обои", 0, Status.NEW);
        manager.addSubTask(subTask1, epicForRepair.getId());
        SubTask subTask2 = new SubTask("subTask2", "узнать, можно ли снести стену", 0, Status.NEW);
        manager.addSubTask(subTask2, epicForRepair.getId());


        System.out.println("Полуение списка всех задач");
        ArrayList<Task> tasks = manager.getAllTasks();
        for (Task task : tasks) {
            System.out.println(task);
        }

        System.out.println("---------------------------------------------------------------------------------------");

        System.out.println("Подзадачи для эпика " + epicForRepair.description + ":");
        ArrayList<Task> subTasks = manager.getAllSubTaskForEpic(epicForRepair.getId());
        for (Task task : subTasks) {
            System.out.println(task);
        }

        System.out.println("Вывожу задачу по id");
        System.out.println(manager.getTaskById(1));

        SubTask newSubTask1 = new SubTask("подзадача 1", "обновленная задача", 1, Status.IN_PROGRESS);
        manager.updateSubTask(newSubTask1, epicForRepair.getId());

        System.out.println("---------------------------------------------------------------------------------------");

        System.out.println("Полуение списка всех задач");
        tasks = manager.getAllTasks();
        for (Task task : tasks) {
            System.out.println(task);
        }

        System.out.println("---------------------------------------------------------------------------------------");

        System.out.println("Удаление сабтаски");
        manager.deleteSubTask(newSubTask1);

        System.out.println("Полуение списка всех задач");
        tasks = manager.getAllTasks();
        for (Task task : tasks) {
            System.out.println(task);
        }


    }
}
