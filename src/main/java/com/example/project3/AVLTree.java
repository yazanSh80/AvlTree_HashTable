package com.example.project3;

import java.util.ArrayList;
import java.util.List;

public class AVLTree {
    private Node root;
    private int size;
    List<String> districts;
    List<String> locations;
    List<Martyr> martyrs;

    public AVLTree() {
        this.districts = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.martyrs = new ArrayList<>();
        this.size = 0;
    }

    public void insertDistrict(String district) {
        if (!districts.contains(district)) {
            districts.add(district);
        }
    }

    public List<String> getDistricts() {
        return districts;
    }

    public void insertLocation(String location) {
        if (!locations.contains(location)) {
            locations.add(location);
        }
    }

    public List<String> getLocation() {
        return locations;
    }

    public int calculateHeight() {
        return calculateHeight(root);
    }

    private int calculateHeight(Node node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = calculateHeight(node.left);
        int rightHeight = calculateHeight(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public int getSize() {
        return size;
    }
    public List<Martyr> printLevelByLevel() {
        if (root == null) {
            return null;
        }
        List<Martyr> result = new ArrayList<>();
        Queue queue = new Queue();
        queue.add(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Martyr> currentLevel = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                Node current = queue.poll();
                currentLevel.add(current.data);
                if (current.left != null) {
                    queue.add(current.left);
                }
                if (current.right != null) {
                    queue.add(current.right);
                }
            }
            result.addAll(currentLevel);
        }

        return result;
    }

    public List<Martyr> inOrderTraversal(Node node, List<Martyr> elements) {
        if (node == null) {
            return elements;
        }

        inOrderTraversal(node.left, elements);
        elements.add(node.data);
        inOrderTraversal(node.right, elements);

        return elements;
    }

    public List<Martyr> getAllMartyrs() {
        List<Martyr> martyrs = new ArrayList<>();
        inOrderTraversal(root, martyrs);
        return martyrs;
    }

    public List<Martyr> getAllMartyrsSortedByAge() {
        List<Martyr> martyrs = new ArrayList<>();
        inOrderTraversal(root, martyrs);
        heapSort(martyrs);
        return martyrs;
    }
    private void heapSort(List<Martyr> martyrs) {
        int n = martyrs.size();
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(martyrs, n, i);
        }
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            Martyr temp = martyrs.get(0);
            martyrs.set(0, martyrs.get(i));
            martyrs.set(i, temp);

            heapify(martyrs, i, 0);
        }
    }

    private void heapify(List<Martyr> martyrs, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && martyrs.get(l).getAge() > martyrs.get(largest).getAge()) {
            largest = l;
        }

        if (r < n && martyrs.get(r).getAge() > martyrs.get(largest).getAge()) {
            largest = r;
        }

        if (largest != i) {
            Martyr temp = martyrs.get(i);
            martyrs.set(i, martyrs.get(largest));
            martyrs.set(largest, temp);
            heapify(martyrs, n, largest);
        }
    }

    public void insert(Martyr data) {
        root = insert(root, data);
        if (martyrs == null) {
            martyrs = new ArrayList<>();
        }
        martyrs.add(data);
    }

    private Node insert(Node node, Martyr data) {
        if (node == null) {
            size++;
            return new Node(data);
        }
        int comparison = data.getName().compareTo(node.data.getName());
        if (comparison < 0) {
            node.left = insert(node.left, data);
        } else if (comparison > 0) {
            node.right = insert(node.right, data);
        } else {
            return node;
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balance(node);
    }
    public void delete(Martyr martyr) {
        root = delete(root, martyr);
        if (martyrs == null) {
            System.out.println("Nothing to delete");
        }
        martyrs.remove(martyr);
    }
    private Node delete(Node node, Martyr martyr) {
        if (node == null) {
            return node;
        }
        int comparison = martyr.getName().compareTo(node.data.getName());
        if (comparison < 0) {
            node.left = delete(node.left, martyr);
        } else if (comparison > 0) {
            node.right = delete(node.right, martyr);
        } else {
            if (node.left == null || node.right == null) {
                Node temp;
                if (node.left != null) {
                    temp = node.left;
                } else {
                    temp = node.right;
                }
                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    node = temp;
                }
                size--;
            }
            else {
                Node temp = minValueNode(node.right);
                node.data = temp.data;
                node.right = delete(node.right, temp.data);
            }
        }
        if (node == null) {
            return node;
        }
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Balance the node
        return balance(node);
    }
//    public void update(Martyr oldMartyr, Martyr newMartyr) {
//        if (oldMartyr == null || newMartyr == null) return;
//        int index = martyrs.indexOf(oldMartyr);
//        if (index!= -1) {
//            martyrs.set(index, newMartyr); // Update the martyr in the list
//        }
//        root = updateNode(root, oldMartyr, newMartyr); // Update the node in the AVL tree
//    }
//
//    private Node updateNode(Node node, Martyr oldMartyr, Martyr newMartyr) {
//        if (node == null) {
//            return node; // Not found
//        }
//        int comparison = oldMartyr.getName().compareTo(node.data.getName());
//        if (comparison < 0) {
//            node.left = updateNode(node.left, oldMartyr, newMartyr); // Recursively update on the left
//        } else if (comparison > 0) {
//            node.right = updateNode(node.right, oldMartyr, newMartyr); // Recursively update on the right
//        } else {
//            // Node to update found
//            node.data = newMartyr; // Update the node's data
//        }
//        return node;
//    }
    public int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private Node balance(Node node) {
        int balance = getBalance(node);

        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node);
        }

        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);
        }

        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private int getBalance(Node node) {
        if (node == null) {
            return 0;
        } else {
            return height(node.left) - height(node.right);
        }
    }

    private Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    public List<Martyr> getSortedMartyrs() {
        return getAllMartyrs(); // Assuming sorted by in-order traversal
    }
}
