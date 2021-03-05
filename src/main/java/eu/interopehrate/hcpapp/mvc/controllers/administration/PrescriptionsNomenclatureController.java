package eu.interopehrate.hcpapp.mvc.controllers.administration;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.administration.PrescriptionsNomenclatureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administration/prescriptions-nomenclature")
public class PrescriptionsNomenclatureController {
    private final PrescriptionsNomenclatureService prescriptionsNomenclatureService;

    public PrescriptionsNomenclatureController(PrescriptionsNomenclatureService prescriptionsNomenclatureService) {
        this.prescriptionsNomenclatureService = prescriptionsNomenclatureService;
    }

    @GetMapping
    @RequestMapping("/view-details")
    public String detailsTemplate(Model model) {
        model.addAttribute("prescriptionTypes", this.prescriptionsNomenclatureService.getPrescriptionTypes());
        return TemplateNames.ADMINISTRATION_PRESCRIPTIONS_NOMENCLATURE_VIEW_DETAILS;
    }

//    @PostMapping
//    @RequestMapping("/save-add")
//    public String saveAdd(@Valid @ModelAttribute("vitalSignsType") VitalSignsTypesEntity vitalSignsTypesEntity, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return TemplateNames.ADMINISTRATION_VITAL_SIGNS_NOMENCLATURE_VIEW_DETAILS;
//        }
//        this.vitalSignsNomenclatureService.addVitalSignsType(vitalSignsTypesEntity);
//        return "redirect:/administration/vital-signs-nomenclature/view-details";
//    }
}
