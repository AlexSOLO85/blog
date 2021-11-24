package main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController {
    public static final String PATH = "/**/{path:[^\\.]*}";

    @RequestMapping("/")
    public final String index(final Model model) {
        return "index";
    }

    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET},
            value = PATH)
    public final String redirect(
            final @PathVariable String path) {
        return "forward:/";
    }
}
