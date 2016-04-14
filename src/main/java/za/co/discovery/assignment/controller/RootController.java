package za.co.discovery.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import za.co.discovery.assignment.entity.Vertex;
import za.co.discovery.assignment.helper.Graph;
import za.co.discovery.assignment.model.ShortestPathModel;
import za.co.discovery.assignment.service.EntityManagerService;
import za.co.discovery.assignment.service.ShortestPathService;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Kapeshi.Kongolo on 2016/04/09.
 */
@Controller
public class RootController {

    private EntityManagerService entityManagerService;
    private ShortestPathService shortestPathService;

    @Autowired
    public RootController(EntityManagerService entityManagerService, ShortestPathService shortestPathService) {
        this.entityManagerService = entityManagerService;
        this.shortestPathService = shortestPathService;
    }

    @RequestMapping(value = "/vertices", method = RequestMethod.GET)
    public String list(Model model) {
        ArrayList allVertices = entityManagerService.getAllVertices();
        model.addAttribute("vertices", allVertices);
        return "vertices";
    }

    @RequestMapping(value = "/path", method = RequestMethod.GET)
    public String getPlanets(Model model) {
        ArrayList allVertices = entityManagerService.getAllVertices();
        model.addAttribute("destination", allVertices);
        return "path";
    }

    @RequestMapping(value="/shortest", method=RequestMethod.GET)
    public String shortestForm(Model model) {
        ShortestPathModel s = new ShortestPathModel();
        ArrayList allVertices = entityManagerService.getAllVertices();
        Vertex origin = (Vertex) allVertices.get(0);
        s.setVertexName(origin.getName());
        model.addAttribute("shortest", s);
        model.addAttribute("pathList", allVertices);
        return "shortest";
    }

    @RequestMapping(value="/shortest", method=RequestMethod.POST)
    public String shortestSubmit(@ModelAttribute ShortestPathModel s, Model model) {

        StringBuilder path = new StringBuilder();
        Graph graph = entityManagerService.selectGraph();
        shortestPathService.initializePlanets(graph);
        Vertex destination = entityManagerService.getVertexById(s.getSelectedVertex());
        Vertex source = entityManagerService.getVertexByName(s.getVertexName());
        //
        shortestPathService.run(source);
        LinkedList<Vertex> paths = shortestPathService.getPath(destination);
        if(paths!=null){
            for (Vertex v : paths) {
                path.append(v.getName());
                path.append("\t\t");
            }
        }
        s.setThePath(path.toString());
        s.setSelectedVertexName(destination.getName());
        model.addAttribute("shortest", s);
        return "result";
    }

    @RequestMapping("vertex/{vertexId}")
    public String showVertex(@PathVariable String vertexId, Model model) {
        model.addAttribute("vertex", entityManagerService.getVertexById(vertexId));
        return "vertexshow";
    }

    @RequestMapping("vertex/edit/{vertexId}")
    public String edit(@PathVariable String vertexId, Model model) {
        model.addAttribute("vertex", entityManagerService.getVertexById(vertexId));
        return "vertexform";
    }

    @RequestMapping("vertex/new")
    public String newVertex(Model model) {
        model.addAttribute("vertex", new Vertex());
        return "vertexform";
    }

    @RequestMapping(value = "vertex", method = RequestMethod.POST)
    public String saveVertex(Vertex vertex) {
        entityManagerService.updateVertex(vertex);
        return "redirect:/vertex/" + vertex.getVertexId();
    }

    @RequestMapping("vertex/delete/{vertexId}")
    public String delete(@PathVariable String vertexId) {
        entityManagerService.deleteVertex(vertexId);
        return "redirect:/vertices";
    }

}

