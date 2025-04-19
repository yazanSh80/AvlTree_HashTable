package com.example.project3;

public class LinkedList {
    private LinkedListNode head;
    private LinkedListNode tail;

    public LinkedList() {
        this.head = null;
        this.tail = null;
    }

    public void add(Node data) {
        LinkedListNode newNode = new LinkedListNode(data);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
    }

    public Node poll() {
        if (head == null) {
            return null;
        }
        Node data = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        return data;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        int count = 0;
        LinkedListNode current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }
}
