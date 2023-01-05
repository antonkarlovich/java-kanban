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
        remove(task.getId());
        linkLast(task);
        getTasks();
    }

    @Override
    public void remove(int id) {
        Node<Task> taskToDel = tasksMap.get(id);
        removeNode(taskToDel);
        getTasks();
        tasksMap.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        return tasks;
    }

    void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(task, oldTail, null);
        tasksMap.put(task.getId(), newNode);
        tail = newNode;
        if(oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        size++;
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

class Node<T> {
    T data;
    Node<T> prev;
    Node<T> next;

    public Node(T data, Node<T> prev, Node<T> next) {
        this.data = data;
        this.prev = prev;
        this.next = next;
    }
}