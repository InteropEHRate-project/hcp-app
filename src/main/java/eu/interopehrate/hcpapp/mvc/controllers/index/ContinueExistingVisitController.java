package eu.interopehrate.hcpapp.mvc.controllers.index;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("session")
@RequestMapping("/index")
public class ContinueExistingVisitController {

    @GetMapping
    @RequestMapping({"/existingVisit"})
    public String indexTemplate() {
        return TemplateNames.INDEX_EXISTING_VISIT;
    }
}
