package es.unizar.sl.p3;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/")
    public String welcome(Model model) {
        model.addAttribute("message", "Hello, World!");
        return "welcome";
    }
}