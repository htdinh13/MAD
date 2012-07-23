/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

/**
 *
 * @author kem
 */
public class LinkedList {

    public Node head;

    public LinkedList() {
        head = null;
    }

    public void addToHead(Cell cell) {
        Node newNode = new Node(cell);
        newNode.next = head;
        head = newNode;
    }

    public void addToHead(Node node) {
        node.next = head;
        head = node;
    }

    public int findNode(Cell cell) {
        int index = 0;
        Node current = head;
        while (current != null) {
            if (current.data.getX() == cell.getX() && current.data.getY() == cell.getY()) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    public Node getNodeByIndex(int index) {
        if (index >= 0) {
            int count = 0;
            Node current = head;
            while (current != null) {
                if (count == index) {
                    return current;
                }
                current = current.next;
                count++;
            }
        }
        return null;
    }

    public void removeFromHead() {
        if (head != null) {
            head = head.next;
        }
    }

    public boolean removeNode(Node node) {
        Node current = head;
        Node previous = null;

        while (current != null) {
            if (current == node) {
                if (current == head) {
                    head = head.next;
                } else {
                    previous.next = current.next;
                }
                return true;
            }

            previous = current;
            current = current.next;
        }

        return false;

    }

    public Node getMinFScore() {
        int minFScore = 375;
        Node current = head;
        Node minFSNode = null;
        while (current != null) {
            if (current.fScore < minFScore) {
                minFScore = current.fScore;
                minFSNode = current;
            }
            current = current.next;
        }
        return minFSNode;
    }

    public boolean isEmpty() {
        return head == null;
    }
}
