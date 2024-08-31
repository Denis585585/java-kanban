package manager;

import data.Epic;
import data.Subtask;
import data.Task;
import data.TaskType;
import util.Status;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;
    private static final String HEAD = "id,type,name,status,description,epic\n";

    public FileBackedTaskManager(File file) {
        super(new InMemoryHistoryManager());
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        int generatorId = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (line.isBlank()) {
                    break;
                }
                Task task = fromString(line);
                if (task instanceof Epic) {
                    fileBackedTaskManager.addEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    fileBackedTaskManager.addSubtask((Subtask) task);
                } else if (task != null) {
                    fileBackedTaskManager.addTask(task);
                } else {
                    System.out.println("Это не является задачей!");
                }
                if (task != null && generatorId < task.getId()) {
                    generatorId = task.getId();
                }
            }
        } catch (IOException e) {
            System.out.println("Не удалось загрузить данные из файла.");
        }
        return fileBackedTaskManager;
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
            fileWriter.write(HEAD);
            for (Task task : showAllTask()) {
                fileWriter.write(toString(task));
            }
            for (Epic epic : showAllEpic()) {
                fileWriter.write(toString(epic));
            }
            for (Subtask subtask : showAllSubtasks()) {
                fileWriter.write(toString(subtask));
            }

        } catch (IOException e) {
            System.out.println("Не удалось сохранить файл");
        }
    }

    private static TaskType getType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof Subtask) {
            return TaskType.SUBTASK;
        }
        return TaskType.TASK;
    }

    private static String toString(Task task) {
        String[] arraysCSV = {Integer.toString(task.getId()),
                getType(task).toString(), task.getTitle(), task.getStatus().toString(), task.getDescription()};
        return String.join(",", arraysCSV);
    }

    private static Task fromString(String value) {
        String[] lines = value.split(",");
        int id = Integer.parseInt(lines[0]);
        String type = lines[1];
        String title = lines[2];
        Status status = Status.valueOf(lines[3]);
        String description = lines[4];
        if (type.equals("EPIC")) {
            return new Epic(id, title, status, description);
        } else if (type.equals("SUBTASK")) {
            int epicId = Integer.parseInt(lines[10]);
            return new Subtask(id, title, status, description, epicId);
        } else {
            return new Task(id, title, status, description);
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subTask) {
        super.addSubtask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subTask) {
        super.updateSubtask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }
}
