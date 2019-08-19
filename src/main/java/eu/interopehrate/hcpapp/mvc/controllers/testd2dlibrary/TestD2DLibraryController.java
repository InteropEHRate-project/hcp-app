package eu.interopehrate.hcpapp.mvc.controllers.testd2dlibrary;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test-d2d-library")
public class TestD2DLibraryController {
    @GetMapping
    @RequestMapping("/view-test-d2d-library")
    public String viewTestD2DLibrary() {
        return TemplateNames.TEST_D2D_LIBRARY_VIEW;
    }
}
