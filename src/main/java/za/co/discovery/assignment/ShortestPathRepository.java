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

    private EntityManagerService entityManagerService;
    private Graph graph;

    @Autowired
    @Qualifier("transactionManager")
    protected PlatformTransactionManager platformTransactionManager;

    @Autowired
    public ShortestPathRepository(EntityManagerService entityManagerService) {
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

    public String getShortestPath(String name){
        StringBuilder path = new StringBuilder();
        ShortestPathService sp =  new ShortestPathService(graph);
        Vertex destination = entityManagerService.getVertexByName(name);
        if(destination==null){
            destination = entityManagerService.getVertexById(name);
            if(destination == null){
                return "The Planet requested does not exist.";
            }
        }
        Vertex source = graph.getVertexes().get(0);
        sp.run(source);
        LinkedList<Vertex> paths = sp.getPath(destination);
        if(paths!=null){
            for (Vertex v : paths) {
                path.append(v.getName());
                path.append("\t");
            }
        }

        return path.toString();
    }
}
