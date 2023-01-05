package main.manager;

import main.exeption.ManagerSaveException;
import main.tasks.Epic;
import main.tasks.Status;
import main.tasks.Subtask;
import main.tasks.Task;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    final private String historyFile;
    final String path = "src/files/";

    public FileBackedTasksManager(String file) {
        historyFile = file;
    }

    private void save() throws ManagerSaveException {
        try{
            Files.createDirectory(Paths.get(path));
        } catch (Exception e) {
            e.getStackTrace();
        }
        try(Writer writer = new FileWriter(historyFile)) {
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
            System.out.println(e.getMessage());
            e.getStackTrace();
            throw new ManagerSaveException("Ошибка записи!");
        }
    }

    private static Task taskFromString(String taskString) {
        String[] fields = taskString.split(",");
        Task task;
        switch (fields[1]) {
            case "TASK":
                task = new Task(fields[2], fields[4], Status.valueOf(fields[3]));
                task.setId(Integer.parseInt(fields[0]));
                break;
            case "SUBTASK":
                task = new Subtask(fields[2], fields[4], Status.valueOf(fields[3]), Integer.parseInt(fields[5]));
                task.setId(Integer.parseInt(fields[0]));
                break;
            case "EPIC":
                task = new Epic(fields[2], fields[4]);
                task.setId(Integer.parseInt(fields[0]));
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
            Map<Integer, List<Integer>> subtasksToEpic = new HashMap<>();

            for (int i = 1; i < tasksStrings.length; i++) {
                String type = tasksStrings[i].split(",")[1];
                switch (type) {
                    case "TASK":
                        fileBackedTasksManager.addNewTask(taskFromString(tasksStrings[i]));
                        break;
                    case "SUBTASK":
                        Subtask subtask = (Subtask) taskFromString(tasksStrings[i]);
                        List<Integer> subtasks = subtasksToEpic.getOrDefault(subtask.getEpicID(), null);
                        if (subtasks != null) {
                            subtasks.add(subtask.getId());
                        } else {
                            subtasks = new ArrayList<>();
                            subtasks.add(subtask.getId());
                        }
                        subtasksToEpic.put(subtask.getEpicID(), subtasks);
                        fileBackedTasksManager.addNewSubtask(subtask);
                        break;
                    case "EPIC":
                        Epic epic = (Epic) taskFromString(tasksStrings[i]);
                        List<Integer> subtasksList = subtasksToEpic.getOrDefault(epic.getId(), null);
                        if (subtasksList != null) {
                            for (Integer id : subtasksList) {
                                epic.addSubtasksId(id);
                            }
                        }
                        fileBackedTasksManager.addNewEpic(epic);
                        break;
                    default:
                        System.out.println("Что-то пошло не так...и задача из файла не создалась!!!");
                }
            }

            for (Integer id : historyIds) {
                fileBackedTasksManager.getTask(id);
                fileBackedTasksManager.getSubtask(id);
                fileBackedTasksManager.getEpic(id);
            }
        } catch (ManagerSaveException e) {  // Вот тут не совсем понял, Reader требует отлавливать IOException
            System.out.println(e.getMessage());
        } catch (IOException e) {
            return fileBackedTasksManager;
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
