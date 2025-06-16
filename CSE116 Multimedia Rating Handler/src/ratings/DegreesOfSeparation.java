package ratings;
import ratings.datastructures.Graph;

import java.util.ArrayList;
import java.util.LinkedList;

public class DegreesOfSeparation {
    private ArrayList<Movie> movies;
    private Graph<String> graph;
    LinkedList<String> queue;

    public DegreesOfSeparation(ArrayList<Movie> movies){
        this.movies = movies;
        this.graph = new Graph<String>();
        this.queue = new LinkedList<String>();
    }
    public int degreesOfSeparation(String name1, String name2){
        for(Movie m : movies){
            for(int i = 0; i < m.getCast().size(); i++){
                for(int c = i; c < m.getCast().size(); c++){
                    if(!m.getCast().get(i).equals(m.getCast().get(c)))
                        graph.addBidirectionalEdge(m.getCast().get(i), m.getCast().get(c));
                }
            }
        }

        int baconNum = graph.bfs(name1, name2);

        return baconNum;
    }
}
