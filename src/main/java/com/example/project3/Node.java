package com.example.project3;

public class Node {
    Martyr data;
    Node left;
    Node right;
    int height;

    public Node(Martyr data) {
        this.data = data;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}