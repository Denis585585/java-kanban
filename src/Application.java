import dao.Dao;
import dao.TaskDao;
import model.EpicTask;
import model.Subtask;
import model.Task;
import service.TaskManagerImpl;
import util.TaskStatusEnum;

import java.util.HashMap;

public class Application {
    public static void main(String[] args) {
        Dao<Integer, Task> dao = new TaskDao();
        TaskManagerImpl taskManager = new TaskManagerImpl(dao);


        Task task1 = new Task("Переезд", "Переезд в Японию", TaskStatusEnum.NEW);
        Task task2 = new Task("Покупки", "Покупка продуктов", TaskStatusEnum.IN_PROGRESS);


        Subtask epicSubtask1 = new Subtask("Домашняя работа1", "Канбан доска", TaskStatusEnum.IN_PROGRESS);
        Subtask epicSubtask2 = new Subtask("Домашняя работа2", "Трекер каллорий", TaskStatusEnum.IN_PROGRESS);

        HashMap<Integer, Subtask> subtaskForEpic1 = new HashMap<>();
        subtaskForEpic1.put(epicSubtask1.getId(), epicSubtask1);
        subtaskForEpic1.put(epicSubtask2.getId(), epicSubtask2);
        EpicTask epic1 = new EpicTask(
                "Обучение",
                "Учеба на курсе",
                TaskStatusEnum.IN_PROGRESS,
                subtaskForEpic1);

        Subtask epicSubtask = new Subtask("Тренировка", "Грудные мышцы", TaskStatusEnum.DONE);

        HashMap<Integer, Subtask> subtaskForEpic2 = new HashMap<>();
        subtaskForEpic2.put(epicSubtask.getId(), epicSubtask);
        EpicTask epic2 = new EpicTask(
                "Подкачаться",
                "В зале",
                TaskStatusEnum.IN_PROGRESS,
                subtaskForEpic2);

        EpicTask epic3 = new EpicTask(
                "Купить авто",
                "купить автомобиль",
                TaskStatusEnum.IN_PROGRESS);

        taskManager.add(task1);
        taskManager.add(task2);
        taskManager.add(epic1);
        taskManager.add(epic2);
        taskManager.add(epic3);

        System.out.println("таски до апдейта");
        for (Task task : taskManager.getAll().values()) {
            System.out.println(task);
        }

        System.out.println("try to find task with id = 0");
        System.out.println(taskManager.findById(0));

        System.out.println("try to find subtask with id = 5 from epic with id = 6");
        System.out.println(taskManager.findSubtaskById(6, 5));

        taskManager.update(7, new Task("продать машину", "в ближайшее время", TaskStatusEnum.IN_PROGRESS));
        System.out.println("таски после апдейта");
        for (Task task : taskManager.getAll().values()) {
            System.out.println(task);
        }


        taskManager.updateSubtask(6, 5, new Subtask("тренировка", "грудь", TaskStatusEnum.IN_PROGRESS));
        System.out.println("subtasks after update");
        for (Task task : taskManager.getAll().values()) {
            System.out.println(task);
        }
        taskManager.remove(6);
        System.out.println("tasks after delete");
        for (Task task : taskManager.getAll().values()) {
            System.out.println(task);
        }
        taskManager.removeSubtask(4, 2);
        System.out.println("subtask with id = 2 was deleted from epic with id = 4");
        for (Task task : taskManager.getAll().values()) {
            System.out.println(task);

        }
    }
}
