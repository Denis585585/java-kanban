package manager;

import data.Epic;
import data.Subtask;
import data.Task;
import data.TaskType;
import util.Status;

public class CSVTaskFormat extends InMemoryTaskManager {
    protected static final String HEAD = "id,type,name,status,description,epic\n";

    protected CSVTaskFormat() {
        super(Managers.getDefaultHistory());
    }

    protected static String toString(Task task) {
        String[] arraysCSV = {Integer.toString(task.getId()),
                getType(task).toString(), task.getTitle(), task.getStatus().toString(), task.getDescription(),
                getEpicId(task)};
        return String.join(",", arraysCSV);
    }

    private static String getEpicId(Task task) {
        if (task instanceof Subtask) {
            return Integer.toString(((Subtask) task).getEpicId());
        }
        return "";
    }

    private static TaskType getType(Task task) {
        if (task instanceof Epic) {
            return TaskType.EPIC;
        } else if (task instanceof Subtask) {
            return TaskType.SUBTASK;
        }
        return TaskType.TASK;
    }

    protected static Task fromString(String value) {
        String[] lines = value.split(",");
        int id = Integer.parseInt(lines[0]);
        String type = lines[1];
        String title = lines[2];
        Status status = Status.valueOf(lines[3]);
        String description = lines[4];
        if (type.equals("EPIC")) {
            return new Epic(id, title, status, description);
        } else if (type.equals("SUBTASK")) {
            int epicId = Integer.parseInt(lines[5]);
            return new Subtask(id, title, status, description, epicId);
        } else {
            return new Task(id, title, status, description);
        }
    }
}
