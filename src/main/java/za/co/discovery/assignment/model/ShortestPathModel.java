package za.co.discovery.assignment.model;

import java.io.Serializable;

/**
 * Created by Kapeshi.Kongolo on 2016/04/14.
 */
public class ShortestPathModel implements Serializable {

    private String selectedVertex;
    private String selectedVertexName;
    private String vertexId;
    private String vertexName;
    private String thePath;
    private String sourceVertex;
    private String destinationVertex;

    public String getSelectedVertex() {
        return selectedVertex;
    }

    public void setSelectedVertex(String selectedVertex) {
        this.selectedVertex = selectedVertex;
    }

    public String getVertexId() {
        return vertexId;
    }

    public void setVertexId(String vertexId) {
        this.vertexId = vertexId;
    }

    public String getVertexName() {
        return vertexName;
    }

    public void setVertexName(String vertexName) {
        this.vertexName = vertexName;
    }

    public String getThePath() {
        return thePath;
    }

    public void setThePath(String thePath) {
        this.thePath = thePath;
    }

    public String getSelectedVertexName() {
        return selectedVertexName;
    }

    public void setSelectedVertexName(String selectedVertexName) {
        this.selectedVertexName = selectedVertexName;
    }

    public String getSourceVertex() {
        return sourceVertex;
    }

    public void setSourceVertex(String sourceVertex) {
        this.sourceVertex = sourceVertex;
    }

    public String getDestinationVertex() {
        return destinationVertex;
    }

    public void setDestinationVertex(String destinationVertex) {
        this.destinationVertex = destinationVertex;
    }

}