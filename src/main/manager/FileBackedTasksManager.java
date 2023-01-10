package main.manager;

import main.exeption.ManagerSaveException;
import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.Subtask;
import main.tasks.Task;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager{
    final private String file;
    final String path = "src/files/";

    public FileBackedTasksManager(String file) {
        this.file = file;
    }

    private void save() {//понял) просто я сначала собирал всю конструкцию, блока catch не было, поэтому идея ругалась)
        //а удалить потом забыл
        try{
            Files.createDirectory(Paths.get(path));
        } catch (Exception e) {
            e.getStackTrace();
        }
        try(Writer writer = new FileWriter(file)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task: tasksMap.values()) {
                writer.write(task.toString() + "\n");
            }
            for (Task subtask: subTasksMap.values()) {
                writer.write(subtask.toString() + "\n");
            }
            for (Task epic: epicsMap.values()) {
                writer.write(epic.toString() + "\n");
            }
            writer.write("\n");
            writer.write(historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл " + file);
        }
    }

    private static Task taskFromString(String taskString) {
        String[] fields = taskString.split(",");
        final int id = Integer.parseInt(fields[0]);
        final String type = fields[1];
        final String name = fields[2];
        final Status status = Status.valueOf(fields[3]);
        final String desc = fields[4];

        Task task;
        switch (type) {
            case "TASK":
                task = new Task(name, desc, status);
                task.setId(id);
                break;
            case "SUBTASK":
                task = new Subtask(name, desc, status, Integer.parseInt(fields[5]));
                task.setId(id);
                break;
            case "EPIC":
                task = new Epic(name, desc, status);
                task.setId(id);
                break;
            default:
                task = null;
        }
        return task;
    }

    public static String historyToString(HistoryManager manager){
        List<Task> history = manager.getHistory();
        String historyString = "";

        for (int i = 0; i < history.size(); i++) {
            if (i == history.size() - 1) {
                historyString += history.get(i).getId();
            } else {
                historyString += history.get(i).getId() + ",";
            }
        }
        return historyString;
    }

    static List<Integer> historyFromString(String value) {
        List <Integer> historyList = new ArrayList<>();
        String[] taskIds = value.split(",");
        for (String taskId: taskIds) {
            historyList.add(Integer.parseInt(taskId));
        }
        return historyList;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file.getAbsolutePath());
        try(Reader reader = new FileReader(file.getAbsolutePath())) {
            BufferedReader br = new BufferedReader(reader);
            StringBuilder builder = new StringBuilder();

            while (br.ready()) {
                builder.append(br.readLine());
                builder.append("\n");
            }

            String fileString = builder.toString();
            String[] blocks = fileString.split("\n\n");
            String[] tasksStrings = blocks[0].split("\n");
            List <Integer> historyIds = historyFromString(blocks[1].trim());
            Map <Integer, List<Integer>> subtasksToEpic = new HashMap<>();

            for (int i = 1; i < tasksStrings.length; i++) {
                String type = tasksStrings[i].split(",")[1];
                Task task = taskFromString(tasksStrings[i]);
                switch (type) {
                    case "TASK":
                        fileBackedTasksManager.tasksMap.put(task.getId(), task);
                        break;
                    case "SUBTASK":
                        fileBackedTasksManager.subTasksMap.put(task.getId(), (Subtask) task);
                        break;
                    case "EPIC":
                        fileBackedTasksManager.epicsMap.put(task.getId(), (Epic) task);
                        break;
                    default:
                        System.out.println("Не удалось создать задачу из файла");
                }
            }
            for (Subtask subtask : fileBackedTasksManager.subTasksMap.values()) {
                if (subtask.getEpicID() != null) {
                    fileBackedTasksManager.epicsMap.get(subtask.getEpicID()).addSubtasksId(subtask.getId());
                }
            }

            for (Integer id : historyIds) {
                fileBackedTasksManager.historyManager.add(fileBackedTasksManager.getTask(id));
                fileBackedTasksManager.historyManager.add(fileBackedTasksManager.getSubtask(id));
                fileBackedTasksManager.historyManager.add(fileBackedTasksManager.getEpic(id));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при чтении файла " + file);
        }

        return fileBackedTasksManager;
    }

    @Override
    public void addNewTask(Task task) {
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
    }

    @Override
    public void addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void updateTask(Task newTask) {
        super.updateTask(newTask);
        save();
    }

    @Override
    public void updateSubtask(Subtask newSubtask) {
        super.updateSubtask(newSubtask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }
}
