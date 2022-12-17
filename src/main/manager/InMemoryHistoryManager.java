package main.manager;

import main.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node<Task>> historyMap;
    private Node<Task> head;
    private Node<Task> tail;

    public InMemoryHistoryManager() {
        this.historyMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (task == null)
            return;
        remove(task.getId());//с этими методами не разобрался(
        linkLast(task);

    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        List<Task> taskList = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            taskList.add(current.data);
            current = current.next;
        }
        return taskList;
    }

    private void linkLast(Task task) {
        Node<Task> oldTail = tail;
        Node<Task> newNod = new Node<>(oldTail, task, null);
        tail = newNod;
        historyMap.put(task.getId(), newNod);
        if (oldTail == null) {
            head = newNod;
        } else {
            oldTail.next = newNod;
        }
    }

    private void removeNode(Node<Task> node) {
        if (node != null) {
            final Node<Task> next = node.next;
            final Node<Task> prev = node.prev;
            node.data = null;

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node) {
                head = next;
                head.prev = null;
            } else if (tail == node) {
                tail = prev;
                tail.next = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }

        }
    }
}

class Node<Task> {
    public Task data;
    public Node<Task> next;
    public Node<Task> prev;

    public Node(Node<Task> prev, Task data, Node<Task> next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}