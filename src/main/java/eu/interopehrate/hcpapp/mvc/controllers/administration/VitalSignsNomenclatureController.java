package eu.interopehrate.hcpapp.mvc.controllers.administration;

import eu.interopehrate.hcpapp.jpa.entities.VitalSignsTypesEntity;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.administration.VitalSignsNomenclatureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/administration/vital-signs-nomenclature")
public class VitalSignsNomenclatureController {
    private final VitalSignsNomenclatureService vitalSignsNomenclatureService;

    public VitalSignsNomenclatureController(VitalSignsNomenclatureService vitalSignsNomenclatureService) {
        this.vitalSignsNomenclatureService = vitalSignsNomenclatureService;
    }

    @GetMapping
    @RequestMapping("/view-details")
    public String detailsTemplate(Model model) {
        model.addAttribute("vitalSignsNomenclature", this.vitalSignsNomenclatureService.getVitalSignsTypes());
        model.addAttribute("vitalSignsTypesEntity", new VitalSignsTypesEntity());
        return TemplateNames.ADMINISTRATION_VITAL_SIGNS_NOMENCLATURE_VIEW_DETAILS;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute VitalSignsTypesEntity vitalSignsTypesEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.ADMINISTRATION_VITAL_SIGNS_NOMENCLATURE_VIEW_DETAILS;
        }
        this.vitalSignsNomenclatureService.addVitalSignsType(vitalSignsTypesEntity);
        return "redirect:/administration/vital-signs-nomenclature/view-details";
    }
}
