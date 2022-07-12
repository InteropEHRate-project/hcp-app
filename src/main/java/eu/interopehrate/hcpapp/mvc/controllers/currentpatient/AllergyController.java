package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.AllergyTypesEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.AllergyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/current-patient/allergies")
public class AllergyController {

    public static final String EN = "en";

    private final AllergyService allergyService;

    public AllergyController(AllergyService allergyService) {
        this.allergyService = allergyService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model, HttpSession session) {
        if (Objects.nonNull(CurrentPatient.typeOfWorkingSession)) {
            session.setAttribute("emergency", CurrentPatient.typeOfWorkingSession.toString());
        }
        model.addAttribute("patient", this.allergyService.getCurrentPatient().getPatient());
        model.addAttribute("allergyIntolerance", this.allergyService.allergyInfoCommand());
        model.addAttribute("allergyIntoleranceTranslated", this.allergyService.allergyInfoCommandTranslated());
        model.addAttribute("newAllergies", this.allergyService.listOfNewAllergies());
        return TemplateNames.CURRENT_PATIENT_ALLERGIES_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model, @RequestParam(required = false) String lang) {
        model.addAttribute("allergyInfoCommand", new AllergyInfoCommand());
        List<AllergyTypesEntity> listOfDiseases = null != lang && (lang.equals("en") || lang.equals("fr")
                || lang.equals("it") ) ?
                this.allergyService.getAllergyTypesRepository().findAllByLang(lang) :
                this.allergyService.getAllergyTypesRepository().findAllByLang(EN);

        model.addAttribute("allergyTypes", listOfDiseases);
        return TemplateNames.CURRENT_PATIENT_ALLERGIES_ADD_PAGE;
    }

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute AllergyInfoCommand allergyInfoCommand, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allergyTypes", this.allergyService.getAllergyTypesRepository().findAll());
            return TemplateNames.CURRENT_PATIENT_ALLERGIES_ADD_PAGE;
        }
        this.allergyService.insertAllergy(allergyInfoCommand);
        return "redirect:/current-patient/allergies/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete")
    public String deleteNewAllergy(@RequestParam("id") Long id, Model model, HttpSession session) {
        this.allergyService.deleteNewAllergy(id);
        model.addAttribute("allergyDeleted", Boolean.TRUE);
        return this.viewSection(model, session);
    }

    @GetMapping
    @RequestMapping("/open-update-page")
    public String openEditNewAllergy(@RequestParam("id") Long id, Model model) {
        model.addAttribute("allergyInfoCommand", this.allergyService.retrieveNewAllergyById(id));
        model.addAttribute("allergyTypes", this.allergyService.getAllergyTypesRepository().findAll());
        return TemplateNames.CURRENT_PATIENT_ALLERGIES_UPDATE_PAGE;
    }

    @PutMapping
    @RequestMapping("/update")
    public String updateNewAllergy(@Valid @ModelAttribute("allergyInfoCommand") AllergyInfoCommand allergyInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_ALLERGIES_UPDATE_PAGE;
        }
        this.allergyService.updateNewAllergy(allergyInfoCommand);
        return "redirect:/current-patient/allergies/view-section";
    }

    @GetMapping
    @RequestMapping("/open-update-page-data-s-ehr")
    public String openEditAllergyFromSEHR(@RequestParam("id") String id, Model model) {
        model.addAttribute("allergyInfoCommand", this.allergyService.retrieveAllergyFromSEHRById(id));
        model.addAttribute("allergyTypes", this.allergyService.getAllergyTypesRepository().findAll());
        return TemplateNames.CURRENT_PATIENT_ALLERGIES_UPDATE_SEHR_PAGE;
    }

    @PutMapping
    @RequestMapping("/update-data-s-ehr")
    public String updateAllergyFromSEHR(@Valid @ModelAttribute("allergyInfoCommand") AllergyInfoCommand allergyInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_ALLERGIES_UPDATE_SEHR_PAGE;
        }
        this.allergyService.updateAllergyFromSEHR(allergyInfoCommand);
        return "redirect:/current-patient/allergies/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete-data-s-ehr")
    public String deleteAllergyFromSEHR(@RequestParam("id") String id, Model model, HttpSession session) {
        this.allergyService.deleteAllergyFromSEHR(id);
        model.addAttribute("allergyDeletedFromSEHR", Boolean.TRUE);
        return this.viewSection(model, session);
    }

    @GetMapping
    @RequestMapping("/refresh-data")
    public String refreshData() {
        this.allergyService.refreshData();
        return "redirect:/current-patient/allergies/view-section";
    }
}
