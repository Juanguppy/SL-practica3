package es.unizar.sl.p3;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Hello, World!";
    }
}