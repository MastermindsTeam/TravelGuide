package blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ivanov on 24.11.2016 г..
 */
@Controller
public class HomeController {
    @RequestMapping("/")
    public String home(){

        return "home";
    }
}
