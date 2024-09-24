package manager;

import data.Epic;
import data.Subtask;
import data.Task;
import data.TaskType;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;


    public FileBackedTaskManager(File file) {
        super(Managers.getDefaultHistory());
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                Task task = CSVTaskFormat.fromString(line);
                if (task.getType().equals(TaskType.EPIC)) {
                    fileBackedTaskManager.addEpic((Epic) task);
                } else if (task.getType().equals(TaskType.SUBTASK)) {
                    fileBackedTaskManager.addSubtask((Subtask) task);
                } else {
                    fileBackedTaskManager.addTask(task);
                }
                if (fileBackedTaskManager.nextId < task.getId()) {
                    fileBackedTaskManager.nextId = task.getId();
                }
            }
        } catch (IOException e) {
            System.out.println("Не удалось загрузить данные из файла.");
        }
        return fileBackedTaskManager;
    }

    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write(CSVTaskFormat.HEAD);
            for (Task task : getAllTask()) {
                bufferedWriter.write(CSVTaskFormat.toString(task) + "\n");
            }
            for (Epic epic : getAllEpic()) {
                bufferedWriter.write(CSVTaskFormat.toString(epic) + "\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                bufferedWriter.write(CSVTaskFormat.toString(subtask) + "\n");
            }

        } catch (IOException e) {
            System.out.println("Не удалось сохранить файл");
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
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
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
