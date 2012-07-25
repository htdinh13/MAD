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
    private boolean running;

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
            /*
             * while (!open.isEmpty()) { ((Node)open.removeFirst()).reset(); }
             */
        }
        // Go through all the items in the open storage.
        if (closed != null) {
            closed.clear();
            /*
             * while (!closed.isEmpty()) { ((Node)closed.removeFirst()).reset();
             * }
             */
        }
    }

    public LinkedList findPath(Node start, Node goal) {
        this.start = start;
        this.goal = goal;

        // Clear the storages and clean the nodes.
        // Go through all the items in the open storage.
        //resetNodes();
        //open.clear();
        //if(closed != null) closed.clear();

        return startSearch(start, goal);


        //return results;

    }

    protected LinkedList startSearch(Node start, Node goal) {
        // Add the start node to the open list, initialize it.
        start.setParent(null); // make sure it does not have any parent defined.
        start.hScore = start.getEstimatedCostTo(goal, start);
        start.gScore = 0;
        open.add(start);

        // Go through all the items in the open storage.
        int order = 0; // defines the order of which the nodes were visited (used in gui visuals)
        while (!open.isEmpty()) {
            // Let's retrieve the first item from the storage.
            Node node = open.removeFirst();
            node.setVisited(true);
            node.setVisitOrder(order++);

            // Check if we found the goal.
            if (node == goal) {
                return constructPath(node);
            } else {
                // Let's go through all the neighbours of this node.
                Iterator i = node.getNeighbours().iterator();
                while (i.hasNext()) {
                    Node neighbour = i.next();
                    //Node neighbourNode = neighbour;

                    /*
                     * We do not want to visit blocked neighbours, so we skip
                     * them. Also, if the neighbour node is neither in the
                     * closed and the open storage then add it to the open
                     * storage, and set it's parent.
                     */
                    if (!neighbour.isBlocked()) {

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
