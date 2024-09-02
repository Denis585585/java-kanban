package manager;

import java.io.File;

public abstract class Managers {
    public static FileBackedTaskManager getDefaultFileManager(File file) {
        return FileBackedTaskManager.loadFromFile(file);
    }

    public static TaskManager getDefault(File file) {
        return new FileBackedTaskManager(file);
    }

    static InMemoryHistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
