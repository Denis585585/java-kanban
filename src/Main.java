import Data.Epic;
import Data.Subtask;
import Data.Task;
import Manager.Limits;
import Manager.TaskManager;
import util.Status;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager(new Limits());
        Epic epic = new Epic("Epic", "JustDoIT");
        taskManager.addEpic(epic);
        System.out.println("Add epic");
        System.out.println(epic);

        Task task = new Task("Task", "getNewTask");
        taskManager.addTask(task);
        System.out.println(task);
        task.setStatus(Status.DONE);



        Subtask subtask1 = new Subtask("Subtask1 mems ", "Write something", 1);
        taskManager.addSubtask(subtask1);
        System.out.println("Adds first subtask");
        System.out.println(subtask1);
        Subtask subtask2 = new Subtask("Subtask2", "Get", 1);
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
