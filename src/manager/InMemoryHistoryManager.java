package manager;

import data.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private final HandMadeLinkedList history = new HandMadeLinkedList();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        remove(task.getId());
        history.linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public void remove(int id) {
        history.removeNode(id);
    }

    private static class Node {
        private final Task task;
        private Node prev;
        private Node next;

        private Node(Task task, Node prev, Node next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }

    private static class HandMadeLinkedList {
        private Node head;
        private Node tail;
        private final Map<Integer, Node> nodeMap = new HashMap<>();

        private void linkLast(Task task) {
            Node newNode = new Node(task, tail, null);

            if (tail == null) {
                head = newNode;
            } else {
                tail.next = newNode;
            }
            tail = newNode;
            nodeMap.put(task.getId(), newNode);
        }

        private void removeNode(int id) {
            final Node node = nodeMap.remove(id);
            if (node == null) {
                return;
            }
            if (node.prev == null) {
                head = node.next;
                node.next.prev = null;
            } else if (node.next != null) {
                node.next.prev = node.prev;
                node.prev.next = node.next;
            } else {
                tail = node.prev;
                node.prev.next = null;
            }
        }

        private List<Task> getTasks() {
            List<Task> list = new ArrayList<>();
            Node task = head;
            while (task != null) {
                list.add(task.task);
                task = task.next;
            }
            return list;
        }
    }
}



