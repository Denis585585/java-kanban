package manager;

import data.Epic;
import data.Subtask;
import data.Task;
import data.TaskType;
import util.Status;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CSVTaskFormat extends InMemoryTaskManager {
    protected static final String HEAD = "id,type,name,status,description,epic\n";
    protected static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy,HH:mm");
    protected static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    protected static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    protected CSVTaskFormat() {
        super(Managers.getDefaultHistory());
    }

    protected static String toString(Task task) {
        String startTime = task.getStartTime() != null ? task.getStartTime().format(FORMATTER) : "";
        String endTime = task.getEndTime() != null ? task.getEndTime().format(FORMATTER) : "";
        String duration = task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "";
        String[] arraysCSV = {Integer.toString(task.getId()),
                getType(task).toString(), task.getTitle(), task.getStatus().toString(), task.getDescription(),
                startTime, endTime, duration, getEpicId(task)};
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
        LocalDateTime startTime = LocalDateTime.of(LocalDate.parse(lines[5], DATE_FORMATTER),
                LocalTime.parse(lines[6], TIME_FORMATTER));
        Duration duration = Duration.ofMinutes(Long.parseLong(lines[9]));
        if (type.equals("EPIC")) {
            LocalDateTime epicEndTime = LocalDateTime.of(LocalDate.parse(lines[7], DATE_FORMATTER),
                    LocalTime.parse(lines[8], TIME_FORMATTER));
            return new Epic(id, title, description, status, duration, startTime, epicEndTime);
        } else if (type.equals("SUBTASK")) {
            int epicId = Integer.parseInt(lines[10]);
            return new Subtask(id, title, status, description, epicId);
        } else {
            return new Task(id, title, description, status, duration, startTime);
        }
    }
}
