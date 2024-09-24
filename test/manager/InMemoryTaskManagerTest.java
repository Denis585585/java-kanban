package manager;

class InMemoryTaskManagerTest extends TaskManagersTest<InMemoryTaskManager> {

    InMemoryTaskManagerTest() {
        taskManager = new InMemoryTaskManager();
    }
}