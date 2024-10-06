package um.fds.agl.ter24.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @RequestMapping(value = "/") // support of the "/" route
    public String index() {
        return "index"; // index is the name of the template, the view resolver will map it into src/main/resources/templates/index.html.
    }
}
