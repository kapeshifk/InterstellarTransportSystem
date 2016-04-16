package za.co.discovery.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import za.co.discovery.assignment.entity.Edge;
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

    /*Planets Mapping Start*/

    @RequestMapping(value = "/vertices", method = RequestMethod.GET)
    public String listVertices(Model model) {
        ArrayList allVertices = entityManagerService.getAllVertices();
        model.addAttribute("vertices", allVertices);
        return "vertices";
    }

    @RequestMapping("vertex/{vertexId}")
    public String showVertex(@PathVariable String vertexId, Model model) {
        model.addAttribute("vertex", entityManagerService.getVertexById(vertexId));
        return "vertexshow";
    }

    @RequestMapping("vertex/new")
    public String addVertex(Model model) {
        model.addAttribute("vertex", new Vertex());
        return "vertexadd";
    }

    @RequestMapping(value = "vertex", method = RequestMethod.POST)
    public String saveVertex(Vertex vertex, Model model) {
        if (entityManagerService.vertexExist(vertex.getVertexId())) {
            buildVertexValidation(vertex.getVertexId(), model);
            return "validation";
        }
        entityManagerService.saveVertex(vertex);
        return "redirect:/vertex/" + vertex.getVertexId();
    }

    @RequestMapping("vertex/edit/{vertexId}")
    public String editVertex(@PathVariable String vertexId, Model model) {
        model.addAttribute("vertex", entityManagerService.getVertexById(vertexId));
        return "vertexupdate";
    }

    @RequestMapping(value = "vertexupdate", method = RequestMethod.POST)
    public String updateVertex(Vertex vertex) {
        entityManagerService.updateVertex(vertex);
        return "redirect:/vertex/" + vertex.getVertexId();
    }

    @RequestMapping("vertex/delete/{vertexId}")
    public String deleteVertex(@PathVariable String vertexId) {
        entityManagerService.deleteVertex(vertexId);
        return "redirect:/vertices";
    }
    /*Planets Mapping End*/

    /*Shortest Path Mapping Start*/

    @RequestMapping(value = "/shortest", method = RequestMethod.GET)
    public String shortestForm(Model model) {
        ShortestPathModel pathModel = new ShortestPathModel();
        ArrayList allVertices = entityManagerService.getAllVertices();
        Vertex origin = (Vertex) allVertices.get(0);
        pathModel.setVertexName(origin.getName());
        model.addAttribute("shortest", pathModel);
        model.addAttribute("pathList", allVertices);
        return "shortest";
    }

    @RequestMapping(value = "/shortest", method = RequestMethod.POST)
    public String shortestSubmit(@ModelAttribute ShortestPathModel pathModel, Model model) {

        StringBuilder path = new StringBuilder();
        Graph graph = entityManagerService.selectGraph();
        shortestPathService.initializePlanets(graph);
        Vertex destination = entityManagerService.getVertexById(pathModel.getSelectedVertex());
        Vertex source = entityManagerService.getVertexByName(pathModel.getVertexName());
        //
        shortestPathService.run(source);
        LinkedList<Vertex> paths = shortestPathService.getPath(destination);
        if (paths != null) {
            for (Vertex v : paths) {
                path.append(v.getName()+" ("+v.getVertexId()+")");
                path.append("\t\t");
            }
        } else {
            path.append("NOT AVAILABLE!");
        }
        pathModel.setThePath(path.toString());
        pathModel.setSelectedVertexName(destination.getName());
        model.addAttribute("shortest", pathModel);
        return "result";
    }

    /*Shortest Path Mapping End*/

    /*Routes Mapping Start*/

    @RequestMapping(value = "/edges", method = RequestMethod.GET)
    public String listEdges(Model model) {
        ArrayList allEdges = entityManagerService.getAllEdges();
        model.addAttribute("edges", allEdges);
        return "edges";
    }

    @RequestMapping("edge/{recordId}")
    public String showEdge(@PathVariable long recordId, Model model) {
        model.addAttribute("edge", entityManagerService.getEdgeById(recordId));
        return "edgeshow";
    }

    @RequestMapping("edge/delete/{recordId}")
    public String deleteEdge(@PathVariable long recordId) {
        entityManagerService.deleteEdge(recordId);
        return "redirect:/edges";
    }

    @RequestMapping(value = "edge/new", method = RequestMethod.GET)
    public String addEdge(Model model) {
        ShortestPathModel sh = new ShortestPathModel();
        ArrayList allVertices = entityManagerService.getAllVertices();
        model.addAttribute("edge", new Edge());
        model.addAttribute("edgeModel", sh);
        model.addAttribute("routeList", allVertices);
        return "edgeadd";
    }

    @RequestMapping(value = "edge", method = RequestMethod.POST)
    public String saveEdge(Edge edge, @ModelAttribute ShortestPathModel pathModel, Model model) {
        int id = (int) entityManagerService.getMaxRecordId() + 1;
        edge.setRecordId(id);
        edge.setEdgeId(String.valueOf(id));
        edge.setSource(pathModel.getSourceVertex());
        edge.setDestination(pathModel.getDestinationVertex());
        if (entityManagerService.edgeExist(edge)) {
            buildEdgeValidation(pathModel, model);
            return "validation";
        }
        entityManagerService.saveEdge(edge);
        return "redirect:/edge/" + edge.getRecordId();
    }

    @RequestMapping(value = "edge/edit/{recordId}", method = RequestMethod.GET)
    public String editEdge(@PathVariable long recordId, Model model) {
        ShortestPathModel pathModel = new ShortestPathModel();
        ArrayList allVertices = entityManagerService.getAllVertices();
        Edge edgeToEdit = entityManagerService.getEdgeById(recordId);
        pathModel.setSourceVertex(edgeToEdit.getSource());
        pathModel.setDestinationVertex(edgeToEdit.getDestination());
        model.addAttribute("edge", edgeToEdit);
        model.addAttribute("edgeModel", pathModel);
        model.addAttribute("routeList", allVertices);
        return "edgeupdate";
    }

    @RequestMapping(value = "edgeupdate", method = RequestMethod.POST)
    public String updateEdge(Edge edge, @ModelAttribute ShortestPathModel pathModel, Model model) {
        edge.setSource(pathModel.getSourceVertex());
        edge.setDestination(pathModel.getDestinationVertex());
        if (entityManagerService.edgeExist(edge)) {
            buildEdgeValidation(pathModel, model);
            return "validation";
        }
        entityManagerService.updateEdge(edge);
        return "redirect:/edge/" + edge.getRecordId();
    }

    public void buildEdgeValidation(@ModelAttribute ShortestPathModel pathModel, Model model) {
        String sourceName = entityManagerService.getVertexById(pathModel.getSourceVertex()).getName();
        String sourceDestination = entityManagerService.getVertexById(pathModel.getDestinationVertex()).getName();
        String message = "The route from " + sourceName + " (" + pathModel.getSourceVertex() + ") to " + sourceDestination + " (" + pathModel.getDestinationVertex() + ") exists already.";
        model.addAttribute("validationMessage", message);
    }

    public void buildVertexValidation(String vertexId, Model model) {
        String vertexName = entityManagerService.getVertexById(vertexId).getName();
        String message = "Planet " + vertexId + " already exists as " + vertexName;
        model.addAttribute("validationMessage", message);
    }

    /*Routes Mapping End*/

}

