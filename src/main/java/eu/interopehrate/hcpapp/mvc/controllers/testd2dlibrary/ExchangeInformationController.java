package eu.interopehrate.hcpapp.mvc.controllers.testd2dlibrary;

import eu.interopehrate.hcpapp.services.testd2dlibrary.TestD2DLibraryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test-d2d-library/information")
public class ExchangeInformationController {
    private TestD2DLibraryService testD2DLibraryService;

    public ExchangeInformationController(TestD2DLibraryService testD2DLibraryService) {
        this.testD2DLibraryService = testD2DLibraryService;
    }

    @GetMapping
    @RequestMapping("/send")
    public String send() throws Exception {
        testD2DLibraryService.sendMessageToSEHR();
        return "redirect:/test-d2d-library/view-test-d2d-library";
    }

    @GetMapping
    @RequestMapping("/get-last")
    public String getLast() {
        testD2DLibraryService.lastSEHRMessage();
        return "redirect:/test-d2d-library/view-test-d2d-library";
    }
}
