/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

/**
 *
 * @author Luu Manh 13
 */
public class AStar {

    private LinkedList open, closed;
    private Node start, goal;

    public AStar(LinkedList open, LinkedList closed) {
        this.open = open;
        this.closed = closed;
    }

    public AStar(LinkedList open) {
        this.open = open;
        this.closed = null;
    }

    protected LinkedList constructPath(Node node) {
        LinkedList path = new LinkedList();

        while (node.getParent() != null) {
            node.setPartOfPath(true);
            path.addFirst(node);
            node = node.getParent();
        }
        return path;
    }

    public void reset() {
        resetNodes();
    }

    protected void resetNodes() {
        if (goal != null) {
            goal.reset();
        }
        if (start != null) {
            start.reset();
        }
        if (open != null) {
            open.clear();
        }
        if (closed != null) {
            closed.clear();
        }
    }

    public LinkedList findPath(Node start, Node goal) {
        this.start = start;
        this.goal = goal;
        return startSearch(start, goal);
    }

    protected LinkedList startSearch(Node start, Node goal) {
        start.setParent(null);
        start.hScore = start.getEstimatedCostTo(start, goal);
        start.gScore = 0;
        open.add(start);

        int order = 0;
        while (!open.isEmpty()) {
            Node node = open.removeFirst();
            node.setVisited(true);
            node.setVisitOrder(order++);

            if (node == goal) {
                return constructPath(node);
            } else {
                for (int i = 0; i < node.getNeighbours().length; i++) {
                    Node neighbour = node.getNeighbours()[i];
                    if (neighbour != null && !neighbour.isBlocked()) {
                        int costFromStart = node.gScore + node.getCost();
                        boolean inClosed = closed.contains(neighbour);
                        boolean inOpen = open.contains(neighbour);
                        if ((!inOpen && !inClosed) || costFromStart < neighbour.hScore) {
                            neighbour.setParent(node);
                            neighbour.hScore = costFromStart;
                            neighbour.gScore = neighbour.getEstimatedCostTo(goal, start);
                            if (inClosed) {
                                closed.remove(neighbour);
                            }
                            if (!inOpen) {
                                open.add(neighbour);
                            }
                        }
                    }
                }
                closed.add(node);
            }
        }
        return null;
    }
}
