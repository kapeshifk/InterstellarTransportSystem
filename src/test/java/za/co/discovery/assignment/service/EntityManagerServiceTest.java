package za.co.discovery.assignment.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;
import za.co.discovery.assignment.config.DatasourceBean;
import za.co.discovery.assignment.config.PersistenceBean;
import za.co.discovery.assignment.dao.EdgeDao;
import za.co.discovery.assignment.dao.TrafficDao;
import za.co.discovery.assignment.dao.VertexDao;
import za.co.discovery.assignment.entity.Edge;
import za.co.discovery.assignment.entity.Traffic;
import za.co.discovery.assignment.entity.Vertex;
import za.co.discovery.assignment.helper.Graph;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.shazam.shazamcrest.MatcherAssert.assertThat;
import static com.shazam.shazamcrest.matcher.Matchers.sameBeanAs;
import static org.junit.Assert.*;

/**
 * Created by Kapeshi.Kongolo on 2016/04/13.
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TrafficDao.class, EdgeDao.class, VertexDao.class, DatasourceBean.class, PersistenceBean.class},
        loader = AnnotationConfigContextLoader.class)
public class EntityManagerServiceTest {
    @Autowired
    private SessionFactory sessionFactory;
    private EdgeDao edgeDao;
    private VertexDao vertexDao;
    private TrafficDao trafficDao;
    private EntityManagerService entityManagerService;
    private File file;
    private static final String EXCEL_FILENAME = "/test.xlsx";

    @Before
    public void setUp() throws Exception {
        URL resource = getClass().getResource(EXCEL_FILENAME);
        file = new File(resource.toURI());
        edgeDao = new EdgeDao(sessionFactory);
        trafficDao = new TrafficDao(sessionFactory);
        vertexDao = new VertexDao(sessionFactory);
        entityManagerService = new EntityManagerService(vertexDao,edgeDao,trafficDao);
    }

    @Test
    public void testPersistGraph() throws Exception {
        Session session = sessionFactory.getCurrentSession();
        Vertex vertex1 = new Vertex("A", "Earth");
        Vertex vertex2 = new Vertex("B", "Moon");
        Vertex vertex3 = new Vertex("C", "Jupiter");
        Vertex vertex4 = new Vertex("D", "Venus");
        Vertex vertex5 = new Vertex("E", "Mars");

        List<Vertex> expectedVertexes = new ArrayList<>();
        expectedVertexes.add(vertex1);
        expectedVertexes.add(vertex2);
        expectedVertexes.add(vertex3);
        expectedVertexes.add(vertex4);
        expectedVertexes.add(vertex5);

        Edge edge1 = new Edge(1, "1", "A", "B", 0.44f);
        Edge edge2 = new Edge(2, "2", "A", "C", 1.89f);
        Edge edge3 = new Edge(3, "3", "A", "D", 0.10f);
        Edge edge4 = new Edge(4, "4", "B", "H", 2.44f);
        Edge edge5 = new Edge(5, "5", "B", "E", 3.45f);

        List<Edge> expectedEdges = new ArrayList<>();
        expectedEdges.add(edge1);
        expectedEdges.add(edge2);
        expectedEdges.add(edge3);
        expectedEdges.add(edge4);
        expectedEdges.add(edge5);

        Traffic traffic1 = new Traffic("1", "A", "B", 0.30f);
        Traffic traffic2 = new Traffic("2", "A", "C", 0.90f);
        Traffic traffic3 = new Traffic("3", "A", "D", 0.10f);
        Traffic traffic4 = new Traffic("4", "B", "H", 0.20f);
        Traffic traffic5 = new Traffic("5", "B", "E", 1.30f);

        List<Traffic> expectedTraffics = new ArrayList<>();
        expectedTraffics.add(traffic1);
        expectedTraffics.add(traffic2);
        expectedTraffics.add(traffic3);
        expectedTraffics.add(traffic4);
        expectedTraffics.add(traffic5);

        entityManagerService.persistGraph(file);
        Graph graph = entityManagerService.selectGraph();

        List<Edge> readEdges = graph.getEdges();
        List<Vertex> readVertices = graph.getVertexes();
        List<Traffic> readTraffics = graph.getTraffics();

        assertThat(expectedVertexes, sameBeanAs(readVertices));
        assertThat(expectedEdges, sameBeanAs(readEdges));
        assertThat(expectedTraffics, sameBeanAs(readTraffics));

        //Rollback for testing purpose
        session.getTransaction().rollback();
    }

    @Test
    public void testSelectGraph() throws Exception {

    }

}