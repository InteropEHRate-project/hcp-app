package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.repositories.VitalSignsTypesRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.VitalSignsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/current-patient/visit-data/vital-signs")
public class VitalSignsController {
    private final VitalSignsService vitalSignsService;
    private final VitalSignsTypesRepository vitalSignsTypesRepository;
    private static VitalSignsInfoCommand oldVitalSign;

    public VitalSignsController(VitalSignsService vitalSignsService, VitalSignsTypesRepository vitalSignsTypesRepository) {
        this.vitalSignsService = vitalSignsService;
        this.vitalSignsTypesRepository = vitalSignsTypesRepository;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) throws IOException {
        model.addAttribute("patient", this.vitalSignsService.getCurrentPatient().getPatient());
        model.addAttribute("vitalSigns", vitalSignsService.vitalSignsCommand());
        model.addAttribute("vitalSignsUpload", vitalSignsService.vitalSignsUpload());
        model.addAttribute("vitalSignsService", vitalSignsService.getCurrentD2DConnection());
        return TemplateNames.CURRENT_PATIENT_VITAL_SIGNS_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("vitalSignsInfoCommand", new VitalSignsInfoCommand());
        model.addAttribute("vitalSignsTypes", this.vitalSignsTypesRepository.findAll());
        model.addAttribute("correlations", this.vitalSignsService.correlations());
        return TemplateNames.CURRENT_PATIENT_VITAL_SIGNS_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute VitalSignsInfoCommand vitalSignsInfoCommand, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vitalSignsTypes", this.vitalSignsTypesRepository.findAll());
            model.addAttribute("correlations", this.vitalSignsService.correlations());
            return TemplateNames.CURRENT_PATIENT_VITAL_SIGNS_ADD_PAGE;
        }
        vitalSignsService.insertVitalSigns(vitalSignsInfoCommand);
        return "redirect:/current-patient/visit-data/vital-signs/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete")
    public String deleteData(@RequestParam(name = "an") String an, @RequestParam(name = "sample") String sample) throws IOException {
        this.vitalSignsService.deleteVitalSign(an, sample);
        return "redirect:/current-patient/visit-data/vital-signs/view-section";
    }

    @GetMapping
    @RequestMapping("/open-edit-page")
    public String openEditPage(@RequestParam(name = "an") String an, @RequestParam(name = "sample") String sample, Model model) {
        VitalSignsController.oldVitalSign = this.vitalSignsService.getVitalSign(an, sample);
        model.addAttribute("vitalSignsInfoCommand", VitalSignsController.oldVitalSign);
        model.addAttribute("vitalSignsTypes", this.vitalSignsTypesRepository.findAll());

        model.addAttribute("correlations", this.vitalSignsService.correlations());
        return TemplateNames.CURRENT_PATIENT_VITAL_SIGNS_EDIT_PAGE;
    }

    @PutMapping
    @RequestMapping("/edit")
    public String update(@Valid @ModelAttribute VitalSignsInfoCommand newVitalSignsInfoCommand, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("vitalSignsInfoCommand", newVitalSignsInfoCommand);
            return TemplateNames.CURRENT_PATIENT_VITAL_SIGNS_EDIT_PAGE;
        }
        this.vitalSignsService.editVitalSign(newVitalSignsInfoCommand, VitalSignsController.oldVitalSign);
        return "redirect:/current-patient/visit-data/vital-signs/view-section";
    }
}
