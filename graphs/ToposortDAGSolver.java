package graphs;

import java.util.*;

public class ToposortDAGSolver<V> implements ShortestPathSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;
    private final V start;

    public ToposortDAGSolver(Graph<V> graph, V start) {
        this.edgeTo = new HashMap<V, Edge<V>>();
        this.distTo = new HashMap<V, Double>();
        this.start = start;
        Set<V> visited = new HashSet<V>();
        List<V> postOrder = new ArrayList<V>();
        dfs(start, visited, postOrder, graph);
        Collections.reverse(postOrder);
        edgeTo.put(start, null);
        distTo.put(start, 0.0);
        for(int i = 0; i < postOrder.size(); i++) {
            V from = postOrder.get(i);
            for (Edge<V> e : graph.neighbors(from)) {
                V to = e.to();
                double oldDist = distTo.getOrDefault(to, Double.POSITIVE_INFINITY);
                double newDist = distTo.get(from) + e.weight();
                if (newDist < oldDist) {
                    edgeTo.put(to, e);
                    distTo.put(to, newDist);
                }
            }
        }
    }

    // Private helper method to run DFS while saving the post-order
    private void dfs(V curNode, Set<V> visited, List<V> postOrder, Graph graph){
        if(visited.contains(curNode)){
            return;
        }
        visited.add(curNode);
        List<Edge<V>> neighbors = graph.neighbors(curNode);
        for(Edge<V> e : neighbors){
            V to = e.to();
            dfs(to, visited, postOrder, graph);
        }
        postOrder.add(curNode);
    }

    public List<V> solution(V goal) {
        List<V> path = new ArrayList<V>();
        V curr = goal;
        path.add(curr);
        while (edgeTo.get(curr) != null) {
            curr = edgeTo.get(curr).from();
            path.add(curr);
        }
        Collections.reverse(path);
        return path;
    }
}
