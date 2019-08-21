package eu.interopehrate.hcpapp.mvc.controllers.testd2dlibrary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test-d2d-library/information")
public class ExchangeInformationController {
    @GetMapping
    @RequestMapping("/send")
    public String sendDate() {
        return "redirect:/test-d2d-library/view-test-d2d-library";
    }

    @GetMapping
    @RequestMapping("/get-last")
    public String displayDate() {
        return "redirect:/test-d2d-library/view-test-d2d-library";
    }
}
