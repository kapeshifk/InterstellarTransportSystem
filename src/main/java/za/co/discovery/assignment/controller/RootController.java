package za.co.discovery.assignment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Kapeshi.Kongolo on 2016/04/09.
 */
@Controller
public class RootController {

    public RootController() {

    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

}

