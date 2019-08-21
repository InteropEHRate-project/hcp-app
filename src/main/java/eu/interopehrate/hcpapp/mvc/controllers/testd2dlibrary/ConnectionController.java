package eu.interopehrate.hcpapp.mvc.controllers.testd2dlibrary;

import eu.interopehrate.hcpapp.services.testd2dlibrary.TestD2DLibraryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test-d2d-library/connection")
public class ConnectionController {
    private TestD2DLibraryService testD2DLibraryService;

    public ConnectionController(TestD2DLibraryService testD2DLibraryService) {
        this.testD2DLibraryService = testD2DLibraryService;
    }

    @GetMapping
    @RequestMapping("/open")
    public String openConnection() throws Exception {
        testD2DLibraryService.openConnection();
        return "redirect:/test-d2d-library/view-test-d2d-library";
    }

    @GetMapping
    @RequestMapping("/close")
    public String closeConnection() throws Exception {
        testD2DLibraryService.closeConnection();
        return "redirect:/test-d2d-library/view-test-d2d-library";
    }
}
