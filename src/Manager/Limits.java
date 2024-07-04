package Manager;

import Data.Task;

import java.util.ArrayList;

public class Limits {
    private final static int LIMIT_OF_TASKS = 10;
    private final ArrayList<Task> limit = new ArrayList<>();

    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (limit.size() >= LIMIT_OF_TASKS) {
            limit.removeFirst();
        }
        limit.add(task);
    }

    public ArrayList<Task> getLimit() {
        return limit;
    }
}

