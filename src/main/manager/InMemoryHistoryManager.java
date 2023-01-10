package main.manager;

import main.tasks.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> tasks = new ArrayList<>();
    private Map<Integer, Node<Task>> tasksMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;
    private int size = 0;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        removeNode(tasksMap.remove(task.getId()));
        linkLast(task);
        tasksMap.put(task.getId(), tail);
    }

    @Override
    public void remove(int id) {
        removeNode(tasksMap.get(id));
        tasksMap.remove(id);
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

    void linkLast(Task task) {

        Node<Task> oldTail = tail;
        Node<Task> newNod = new Node<>(oldTail, task, null);
        tail = newNod;
        if (oldTail == null) {
            head = newNod;
        } else {
            oldTail.next = newNod;

        }

    }

    void getTasks() {
        tasks.clear();
        Node<Task> nextNode = head;
        while(nextNode != null) {
            tasks.add(nextNode.data);
            nextNode = nextNode.next;
        }
    }

    void removeNode(Node<Task> node) {
        if (tasksMap.containsValue(node)) {
            Node<Task> prevNode = node.prev;
            Node<Task> nextNode = node.next;
            if (prevNode != null)
                prevNode.next = nextNode;
            else
                head = nextNode;
            if (nextNode != null)
                nextNode.prev = prevNode;
            else
                tail = prevNode;
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