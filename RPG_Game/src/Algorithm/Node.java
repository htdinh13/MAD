/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

/**
 *
 * @author kem
 */
public class Node implements Comparable {

    public Cell data;
    public Node next, prev;
    public Node parent;
    public LinkedList neighbours;
    public int nodeID, gScore, hScore, visitOrder;
    public boolean blocked, visited, partOfPath;

    public Node(Cell data, int nodeID) {
        this.data = data;
        this.blocked = data.getCanMove();
        this.nodeID = nodeID;
        next = null;
        parent = null;
        prev = null;
        neighbours = new LinkedList();
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public LinkedList getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(LinkedList neighbours) {
        this.neighbours = neighbours;
    }

    public int getNodeId() {
        return nodeID;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisitOrder(int visitOrder) {
        this.visitOrder = visitOrder;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getVisitOrder() {
        return visitOrder;
    }

    public boolean isPartOfPath() {
        return partOfPath;
    }

    public void setPartOfPath(boolean partOfPath) {
        this.partOfPath = partOfPath;
    }

    public int getEstimatedCostFromStartToGoal() {
        return hScore + gScore;
    }

    public void reset() {
        visited = false;
        visitOrder = 0;
        parent = null;
        partOfPath = false;
        hScore = 0;
    }

    public String toString() {
        return "[Node " + nodeID + " " + getX() + "," + getY() + " (" + (blocked ? " " : "X") + ")]";
    }

    public int getX() {
        return this.data.getX();
    }

    public int getY() {
        return this.data.getY();
    }

    public int getEstimatedCostTo(Node start, Node goal) {
        int estimatecost = Math.abs(start.getX() - goal.getX()) + Math.abs(start.getY() - goal.getY());
        return estimatecost;
    }

    public int getCost() {
        return 1;
    }

    public int compareTo(Object o) {
        return (((Node) o).data.getX() == this.data.getX() && ((Node) o).data.getY() == this.data.getY()) ? 0 : 1;
    }

    public int compareCost(Node n) {
        int cost = this.getEstimatedCostFromStartToGoal() - n.getEstimatedCostFromStartToGoal();
        if (cost > 0) {
            return 1;
        } else if (cost < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
