/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

/**
 *
 * @author Luu Manh 13
 */
class Iterator {

    Node node;

    public Iterator(LinkedList list) {
        this.node = list.head;
    }

    public boolean hasNext() {
        if (node != null) {
            if (node.next == null) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public Node next() {
        if (node != null) {
            return node.next;
        } else {
            return null;
        }
    }
}
