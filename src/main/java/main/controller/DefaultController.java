package main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The type Default controller.
 */
@Controller
public class DefaultController {

    /**
     * Index string.
     *
     * @return the string
     */
    @RequestMapping("/")
    private String index() {
        return "index";
    }
}
