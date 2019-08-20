package eu.interopehrate.hcpapp.mvc.controllers.testd2dlibrary;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test-d2d-library")
public class ConnectionController {
    @GetMapping
    @RequestMapping("/open-connection")
    public String openConnection() {
        return "redirect:/test-d2d-library/view-test-d2d-library";
    }

    @GetMapping
    @RequestMapping("/close-connection")
    public String closeConnection() {
        return "redirect:/test-d2d-library/view-test-d2d-library";
    }
}
