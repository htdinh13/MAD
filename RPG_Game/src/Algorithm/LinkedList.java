
package Algorithm;

public class LinkedList {

    public Node head, last;

    public LinkedList() {
        head = null;
        last = null;
    }

    public void addFirst(Node node) {
        final Node f = head;
        node.next = head;
        head = node;
        if (f == null) {
            last = head;
        } else {
            f.prev = head;
        }
    }

    public void add(Node node) {
        if (!this.contains(node)) {
            if (head != null) {
                final Node l = last;
                l.next = node;
                node.prev = l;
                last = node;
            } else {
                addFirst(node);
            }
        }
    }

    public void clear() {
        while (head != null) {
            (this.removeFirst()).reset();
        }
    }

    public boolean contains(Node node) {
        int index = 0;
        Node current = head;
        while (current != null) {
            if (current.compareTo(node) == 0) {
                return true;
            }
            current = current.next;
            index++;
        }
        return false;
    }

    public boolean isEmpty() {
        return (head == null);
    }

    public boolean remove(Node node) {
        Node current = head;
        Node previous = null;
        while (current != null) {
            if (current == node) {
                if (current.compareTo(head) == 0) {
                    head = head.next;
                    if (head != null) {
                        head.prev = null;
                    } else {
                        last = null;
                    }
                } else {
                    final Node c = current.next;
                    if (c != null) {
                        c.prev = previous;
                    }
                    previous.next = current.next;
                }
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    public Node removeFirst() {
        Node first = head;
        if (head != null) {
            head = first.next;
            if (head != null) {
                head.prev = null;
            } else {
                last = null;
            }
        }
        return first;
    }

    public void print() {
        Node current = head;
        int count = 1;
        while (current != null) {
            System.out.println("\tNode " + count + " (" + current.data.getX() + "," + current.data.getY() + ") H=" + current.estimatedCostToGoal + " G=" + current.costFromStart);
            current = current.next;
            count++;
        }
    }
}