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
import java.util.List;

/**
 * Created by Kapeshi.Kongolo on 2016/04/10.
 */
@Service
public class EntityManagerService {
    private static final String EXCEL_FILENAME = "interstellar.xlsx";
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
        ClassLoader loader = getClass().getClassLoader();
        File file = new File(loader.getResource(EXCEL_FILENAME).getFile());

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
}
