package za.co.discovery.assignment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.discovery.assignment.dao.EdgeDao;
import za.co.discovery.assignment.dao.TrafficDao;
import za.co.discovery.assignment.dao.VertexDao;
import za.co.discovery.assignment.entity.Edge;
import za.co.discovery.assignment.entity.Traffic;
import za.co.discovery.assignment.entity.Vertex;
import za.co.discovery.assignment.helper.Graph;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kapeshi.Kongolo on 2016/04/10.
 */
@Service
public class EntityManagerService {
    private static final String EXCEL_FILENAME = "/interstellar.xlsx";
    private VertexDao vertexDao;
    private EdgeDao edgeDao;
    private TrafficDao trafficDao;

    @Autowired
    public EntityManagerService(VertexDao vertexDao, EdgeDao edgeDao, TrafficDao trafficDao) {
        this.vertexDao = vertexDao;
        this.edgeDao = edgeDao;
        this.trafficDao = trafficDao;
    }

    public void persistGraph() {
        URL resource = getClass().getResource(EXCEL_FILENAME);
        File file1;
        try {
            file1 = new File(resource.toURI());
            persistGraph(file1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void persistGraph(File file) {
        XLSXHandler handler = new XLSXHandler(file);

        List<Vertex> vertices = handler.readVertexes();
        if(vertices!=null && !vertices.isEmpty()) {
            for (Vertex v : vertices) {
                vertexDao.save(v);
            }
        }
        List<Edge> edges = handler.readEdges();
        if(edges!=null && !edges.isEmpty()) {
            for (Edge e : edges) {
                edgeDao.save(e);
            }
        }
        List<Traffic> traffic = handler.readTraffics();
        if(edges!=null && !edges.isEmpty()) {
            for (Traffic t : traffic) {
                trafficDao.save(t);
            }
        }
    }

    public Graph selectGraph() {
        List<Vertex> vertices = vertexDao.selectAll();
        List<Edge> edges = edgeDao.selectAll();
        List<Traffic> traffics = trafficDao.selectAll();

        Graph graph = new Graph(vertices, edges, traffics);

        return graph;
    }

    public Vertex saveVertex(Vertex vertex){
        vertexDao.save(vertex);
        return vertex;
    }

    public Vertex updateVertex(Vertex vertex){
        vertexDao.update(vertex);
        return vertex;
    }

    public boolean deleteVertex(String vertexId){
        vertexDao.delete(vertexId);
        return true;
    }

    public ArrayList getAllVertices(){
     return (ArrayList) vertexDao.selectAll();
    }

    public boolean vertexExist(String vertexId){
        Vertex vertex =  vertexDao.selectUnique(vertexId);
        if(vertex == null){
            return false;
        } else{
            return true;
        }
    }

    public Edge saveEdge(Edge edge){
        edgeDao.save(edge);
        return edge;
    }

    public Edge updateEdge(Edge edge){
        edgeDao.update(edge);
        return edge;
    }

    public boolean deleteEdge(long recordId){
        edgeDao.delete(recordId);
        return true;
    }

    public ArrayList getAllEdges(){
        return (ArrayList) edgeDao.selectAll();
    }

    public Vertex getVertexByName(String name){
        return vertexDao.selectUniqueByName(name);
    }

    public Vertex getVertexById(String vertexId){
        return vertexDao.selectUnique(vertexId);
    }

    public Edge getEdgeById(long recordId){
        return edgeDao.selectUnique(recordId);
    }

    public long getMaxRecordId(){
        return edgeDao.selectMaxRecordId();
    }

    public boolean edgeExist(Edge edge){
        List<Edge> edges =  edgeDao.edgeExists(edge);
        if(edges.isEmpty()){
            return false;
        } else{
            return true;
        }
    }
}
