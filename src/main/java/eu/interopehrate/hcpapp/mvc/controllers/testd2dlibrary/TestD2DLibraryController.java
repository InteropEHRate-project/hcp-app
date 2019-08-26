package eu.interopehrate.hcpapp.mvc.controllers.testd2dlibrary;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.testd2dlibrary.TestD2DLibraryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test-d2d-library")
public class TestD2DLibraryController {
    private TestD2DLibraryService testD2DLibraryService;

    public TestD2DLibraryController(TestD2DLibraryService testD2DLibraryService) {
        this.testD2DLibraryService = testD2DLibraryService;
    }

    @GetMapping
    @RequestMapping("/view-test-d2d-library")
    public String viewTestD2DLibrary(Model model) {
        model.addAttribute("testD2DLibraryCommand", testD2DLibraryService.currentState());
        return TemplateNames.TEST_D2D_LIBRARY_VIEW;
    }
}
