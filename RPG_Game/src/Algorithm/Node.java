/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

/**
 *
 * @author kem
 */
public class Node {

    public Cell data;
    public Node next;
    public Node parent;
    public int fScore, gScore, hScore;

    public Node(Cell data) {
        this.data = data;
        next = null;
        parent = null;
    }

    public Node(Cell cell, int x2, int y2) {
        data = cell;
        next = null;
        parent = null;
        hScore = Math.abs(cell.getX() - x2) + Math.abs(cell.getY() - y2);
        gScore = 0;
        fScore = hScore + gScore;

    }

    public Node(Cell cell, int x2, int y2, int score) {
        data = cell;
        next = null;
        parent = null;
        hScore = 0;
        gScore = score++;
        fScore = hScore + gScore;
    }

    public void computeScore(Node node, int x, int y) {
        parent = node;
        hScore = Math.abs(node.data.getX() - x) + Math.abs(node.data.getY() - y);
        gScore = node.gScore++;
        fScore = hScore + gScore;
    }
}
