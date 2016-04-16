package za.co.discovery.assignment.service;

import org.springframework.stereotype.Service;
import za.co.discovery.assignment.entity.Edge;
import za.co.discovery.assignment.entity.Vertex;
import za.co.discovery.assignment.helper.Graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Kapeshi.Kongolo on 2016/04/09.
 */
@Service
public class ShortestPathService {

    private List<Vertex> vertices;
    private List<Edge> edges;
    private Set<Vertex> visitedVertices;
    private Set<Vertex> unvisitedVertices;
    private Map<Vertex, Vertex> predecessors;
    private Map<Vertex, Float> distance;

    public ShortestPathService() {
    }

    public ShortestPathService(Graph graph) {
        //initialize vertices and edges
        this.vertices = new ArrayList<>(graph.getVertexes());
        if(graph.isTrafficAllowed()){
            graph.processTraffics();
        }
        if(graph.isUndirectedGraph()){
            this.edges = new ArrayList<>(graph.getUndirectedEdges());
        } else{
            this.edges = new ArrayList<>(graph.getEdges());
        }
    }

    public void initializePlanets(Graph graph) {
        this.vertices = new ArrayList<>(graph.getVertexes());
        if(graph.isTrafficAllowed()){
            graph.processTraffics();
        }
        if(graph.isUndirectedGraph()){
            this.edges = new ArrayList<>(graph.getUndirectedEdges());
        } else{
            this.edges = new ArrayList<>(graph.getEdges());
        }
    }

    public void run(Vertex source) {
        distance = new HashMap<>();
        predecessors = new HashMap<>();
        visitedVertices = new HashSet<>();
        unvisitedVertices = new HashSet<>();
        distance.put(source, 0f);
        unvisitedVertices.add(source);
        while (unvisitedVertices.size() > 0) {
            Vertex currentVertex = getVertexWithLowestDistance(unvisitedVertices);
            visitedVertices.add(currentVertex);
            unvisitedVertices.remove(currentVertex);
            findMinimalDistances(currentVertex);
        }
    }

    private Vertex getVertexWithLowestDistance(Set<Vertex> vertexes) {
        Vertex lowestVertex = null;
        for (Vertex vertex : vertexes) {
            if (lowestVertex == null) {
                lowestVertex = vertex;
            } else if (getShortestDistance(vertex) < getShortestDistance(lowestVertex)) {
                lowestVertex = vertex;
            }
        }
        return lowestVertex;
    }

    private void findMinimalDistances(Vertex currentVertex) {
        List<Vertex> adjacentVertices = getNeighbors(currentVertex);
        for (Vertex target : adjacentVertices) {
            if (getShortestDistance(target) > getShortestDistance(currentVertex) + getDistance(currentVertex, target)) {
                distance.put(target, getShortestDistance(currentVertex) + getDistance(currentVertex, target));
                predecessors.put(target, currentVertex);
                unvisitedVertices.add(target);
            }
        }
    }

    private List<Vertex> getNeighbors(Vertex currentVertex) {
        List<Vertex> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            Vertex destination = fromId(edge.getDestination());
            if (edge.getSource().equals(currentVertex.getVertexId()) && !isVisited(destination)) {
                neighbors.add(destination);
            }
        }
        return neighbors;
    }

    public Vertex fromId(final String str) {
        for (Vertex v : vertices) {
            if (v.getVertexId().equalsIgnoreCase(str)) {
                return v;
            }
        }
        Vertex islandVertex = new Vertex();
        islandVertex.setVertexId(str);
        islandVertex.setName("Island "+str);
        return islandVertex;
    }

    private boolean isVisited(Vertex vertex) {
        return visitedVertices.contains(vertex);
    }

    private Float getShortestDistance(Vertex destination) {
        Float d = distance.get(destination);
        if (d == null) {
            return Float.POSITIVE_INFINITY;
        } else {
            return d;
        }
    }

    private float getDistance(Vertex source, Vertex target) {
        for (Edge edge : edges) {
            if (edge.getSource().equals(source.getVertexId()) && edge.getDestination().equals(target.getVertexId())) {
                return edge.getDistance() + edge.getTimeDelay();
            }
        }
        throw new RuntimeException("Error: Something went wrong!");
    }

    public LinkedList<Vertex> getPath(Vertex target) {
        LinkedList<Vertex> path = new LinkedList<>();
        Vertex step = target;
        // return null if path does not exist
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        //Reverse for good ordering
        Collections.reverse(path);
        return path;
    }

}
