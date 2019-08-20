package eu.interopehrate.hcpapp.mvc.controllers.testd2dlibrary;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test-d2d-library")
public class ExchangeInformationController {
    @GetMapping
    @RequestMapping("/send-date")
    public String sendDate() {
        return "redirect:/test-d2d-library/view-test-d2d-library";
    }

    @GetMapping
    @RequestMapping("/display-date")
    public String displayDate() {
        return "redirect:/test-d2d-library/view-test-d2d-library";
    }
}
