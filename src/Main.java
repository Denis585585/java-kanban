import data.Epic;
import data.Subtask;
import data.Task;


import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import util.Status;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Epic epic = new Epic(5, "Epic", Status.NEW, "JustDoIT");
        taskManager.addEpic(epic);
        System.out.println("Add epic");
        System.out.println(epic);

        Task task = new Task(1, "Task", Status.DONE, "getNewTask");
        taskManager.addTask(task);
        System.out.println(task);
        task.setStatus(Status.DONE);



        Subtask subtask1 = new Subtask(1, "Subtask1 mems ", Status.NEW, "Write something", 1);
        taskManager.addSubtask(subtask1);
        System.out.println("Adds first subtask");
        System.out.println(subtask1);
        Subtask subtask2 = new Subtask(2, "Subtask2", Status.DONE, "Get", 1);
        taskManager.addSubtask(subtask2);
        System.out.println("Adds second subtask");
        System.out.println(subtask2);

        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        System.out.println("Changes first subtask in IN_PROGRESS");
        System.out.println(subtask1);
        System.out.println(subtask2);

        System.out.println("Check epic");
        System.out.println(epic);


        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2);
        System.out.println("Changes second subtask in DONE");
        System.out.println(subtask1);
        System.out.println(subtask2);

        System.out.println("Check epic");
        System.out.println(epic);


        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        System.out.println("Changes first subtask in DONE" + subtask1.getStatus());
        System.out.println(subtask1);
        System.out.println(subtask2);

        System.out.println("Check epic and subtasks" + epic.getStatus());
        System.out.println(epic);


        System.out.println("Show all tasks");
        System.out.println(taskManager.showAllSubtasks());
        System.out.println(taskManager.showAllEpic());
        System.out.println(taskManager.showAllTask());

        System.out.println("find subtasks by ID " + taskManager.findSubtaskById(subtask1.getId()));

        taskManager.removeEpicById(1);
        taskManager.removeTaskById(2);
        taskManager.removeSubtaskById(3);
        System.out.println(epic);
        System.out.println(task);
        System.out.println(subtask1);
    }
}
