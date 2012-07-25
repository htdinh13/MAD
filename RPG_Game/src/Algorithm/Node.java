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
    public Node[] neighbours;
    public int nodeID, costFromStart, estimatedCostToGoal, visitOrder;
    public boolean blocked, visited, partOfPath;

    public Node(Cell data, int nodeID) {
        this.data = data;
        this.blocked = !data.getCanMove();
        this.nodeID = nodeID;
        next = null;
        parent = null;
        prev = null;
        neighbours = new Node[4];
        costFromStart = 0;
        estimatedCostToGoal = 0;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node[] getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Node[] neighbours) {
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
        return estimatedCostToGoal + costFromStart;
    }

    public void reset() {
        next = null;
        prev = null;
        visited = false;
        visitOrder = 0;
        parent = null;
        partOfPath = false;
        estimatedCostToGoal = 0;
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

    public int getEstimatedCostTo(Node goal) {
        int estimatecost = Math.abs(this.getX() - goal.getX()) + Math.abs(this.getY() - goal.getY());
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
