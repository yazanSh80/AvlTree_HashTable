package com.example.project3;

public class Queue {
    private LinkedList list;

    public Queue() {
        this.list = new LinkedList();
    }

    public void add(Node data) {
        list.add(data);
    }

    public Node poll() {
        return list.poll();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }
}
