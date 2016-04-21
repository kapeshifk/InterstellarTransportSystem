package za.co.discovery.assignment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import za.co.discovery.assignment.entity.Vertex;
import za.co.discovery.assignment.helper.Graph;
import za.co.discovery.assignment.service.EntityManagerService;
import za.co.discovery.assignment.service.ShortestPathService;

import javax.annotation.PostConstruct;
import java.util.LinkedList;

/**
 * Created by Kapeshi.Kongolo on 2016/04/13.
 */
@Component
public class ShortestPathRepository {

    protected PlatformTransactionManager platformTransactionManager;
    private Graph graph;
    private EntityManagerService entityManagerService;

    @Autowired
    public ShortestPathRepository(@Qualifier("transactionManager") PlatformTransactionManager platformTransactionManager, EntityManagerService entityManagerService) {
        this.platformTransactionManager = platformTransactionManager;
        this.entityManagerService = entityManagerService;
    }

    @PostConstruct
    public void initData() {

        TransactionTemplate tmpl = new TransactionTemplate(platformTransactionManager);
        tmpl.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                entityManagerService.persistGraph();
                graph = entityManagerService.selectGraph();
            }
        });
    }

    public String getShortestPath(String name) {
        StringBuilder path = new StringBuilder();
        ShortestPathService sp = new ShortestPathService(graph);
        Vertex source = graph.getVertexes().get(0);
        Vertex destination = entityManagerService.getVertexByName(name);
        if (destination == null) {
            destination = entityManagerService.getVertexById(name);
            if (destination == null) {
                return "Planet " + name + " does not exist in the Interstellar Transport System.";
            }
        } else if (source != null && destination != null && source.getVertexId().equals(destination.getVertexId())) {
            return "You are already on planet " + source.getName() + ".";
        }

        sp.run(source);
        LinkedList<Vertex> paths = sp.getPath(destination);
        if (paths != null) {
            for (Vertex v : paths) {
                path.append(v.getName() + " (" + v.getVertexId() + ")");
                path.append("\t");
            }
        } else {
            path.append("There is no path to " + destination.getName());
            path.append(".");
        }

        return path.toString();
    }
}
