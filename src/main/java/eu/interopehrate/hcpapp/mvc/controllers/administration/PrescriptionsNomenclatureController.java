package eu.interopehrate.hcpapp.mvc.controllers.administration;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.PrescriptionTypesEntity;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.administration.PrescriptionsNomenclatureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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
        model.addAttribute("prescriptionType", new PrescriptionTypesEntity());
        return TemplateNames.ADMINISTRATION_PRESCRIPTIONS_NOMENCLATURE_VIEW_DETAILS;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute("prescriptionType")PrescriptionTypesEntity prescriptionTypesEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.ADMINISTRATION_PRESCRIPTIONS_NOMENCLATURE_VIEW_DETAILS;
        }
        this.prescriptionsNomenclatureService.addPrescriptionType(prescriptionTypesEntity);
        return "redirect:/administration/prescriptions-nomenclature/view-details";
    }
}
