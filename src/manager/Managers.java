package manager;

public abstract class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }


    static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
