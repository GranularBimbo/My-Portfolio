package ratings.datastructures;
import java.util.*;

public class Graph<N> {
    private HashMap<N, ArrayList<N>> adjacencyList;

    public Graph(){
        this.adjacencyList = new HashMap<>();
    }

    public void addEdge(N from, N to){
        this.addNode(from);
        this.addNode(to);
        this.adjacencyList.get(from).add(to);
    }

    public void addBidirectionalEdge(N node1, N node2){
        this.addNode(node1);
        this.addNode(node2);
        this.adjacencyList.get(node1).add(node2);
        this.adjacencyList.get(node2).add(node1);
    }

    private void addNode(N a){
        if(!this.adjacencyList.containsKey(a))
            this.adjacencyList.put(a, new ArrayList<>());
    }

    public int bfs(N src, N to){
        if (!adjacencyList.containsKey(src) || !adjacencyList.containsKey(to)) {
            // One or both actors are not in the graph
            return -1;
        }

        if(src.equals(to)){
            return 0;
        }

        Queue<N> queue = new LinkedList<>();
        Set<N> visited = new HashSet<>();
        Map<N, Integer> distance = new HashMap<>();

        queue.offer(src);
        visited.add(src);
        distance.put(src, 0);

        while (!queue.isEmpty()) {
            N currentActor = queue.poll();

            for (N neighbor : adjacencyList.get(currentActor)) {
                if (!visited.contains(neighbor)) {
                    queue.offer(neighbor);
                    visited.add(neighbor);
                    distance.put(neighbor, distance.get(currentActor) + 1);

                    if (neighbor.equals(to)) {
                        // Found the target actor, return the degrees of separation
                        return distance.get(neighbor);
                    }
                }
            }
        }

        // Actors are not connected in the graph
        return -1;
    }
}
