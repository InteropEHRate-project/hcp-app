package eu.interopehrate.hcpapp.mvc.controllers.administration;

import eu.interopehrate.hcpapp.mvc.commands.administration.HealthCareOrganizationCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.administration.HealthCareOrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Slf4j
@Controller
@RequestMapping("/administration/health-care-organization")
public class HealthCareOrganizationController {
    private final HealthCareOrganizationService healthCareOrganizationService;

    public HealthCareOrganizationController(HealthCareOrganizationService healthCareOrganizationService) {
        this.healthCareOrganizationService = healthCareOrganizationService;
    }

    @GetMapping
    @RequestMapping("/view-details")
    public String detailsTemplate(Model model) {
        HealthCareOrganizationCommand healthCareOrganizationCommand = this.healthCareOrganizationService.getHealthCareOrganization();
        model.addAttribute("healthCareOrganization", healthCareOrganizationCommand);
        return TemplateNames.ADMINISTRATION_HEALTH_CARE_ORGANIZATION_VIEW_DETAILS;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("certificate", new ArrayList<Byte>());
        return TemplateNames.ADMINISTRATION_HEALTH_CARE_ORGANIZATION_ADD_PAGE;
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String submit(@RequestParam("file") MultipartFile file, ModelMap modelMap) {
        modelMap.addAttribute("file", file);
        Long id = 1L;
        this.healthCareOrganizationService.saveFile(id, file);

        log.info("File uploaded: " + file.getOriginalFilename());
        return "redirect:/administration/health-care-organization/view-details";
    }
}
