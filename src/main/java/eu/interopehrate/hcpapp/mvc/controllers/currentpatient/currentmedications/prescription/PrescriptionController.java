package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.currentmedications.prescription;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.PrescriptionEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/current-patient/current-medications/prescription")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;
    public static PrescriptionCommand prescriptionCommand;
    public static List<PrescriptionEntity> prescriptionEntityList;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model, HttpSession session, String keywordPrescription) throws IOException {
        if (Objects.nonNull(CurrentPatient.typeOfWorkingSession)) {
            session.setAttribute("emergency", CurrentPatient.typeOfWorkingSession.toString());
        }
        this.prescriptionService.setEmpty(false);
        this.prescriptionService.setFiltered(false);
        session.setAttribute("keywordPrescription", keywordPrescription);
        model.addAttribute("patient", this.prescriptionService.getCurrentPatient().getPatient());
        return this.findPaginated(1, keywordPrescription, model);
    }

    @GetMapping
    @RequestMapping("/view-section/page/{pageNoSEHR}/{keywordPrescription}")
    public String findPaginated(@PathVariable(value = "pageNoSEHR") int pageNoSEHR,
                                @PathVariable(value = "keywordPrescription") String keywordPrescription,
                                Model model) throws IOException {
        model.addAttribute("currentPatient", this.prescriptionService.getCurrentPatient());
        //PAGE SIZE is hardcoded HERE
        int pageSize = 3;
        PrescriptionController.prescriptionCommand = this.prescriptionService.prescriptionCommand(pageNoSEHR, pageSize, keywordPrescription);
        List<PrescriptionInfoCommand> listPrescriptionsSEHR = prescriptionCommand.getPageInfoCommand().getContent();
        model.addAttribute("prescriptionCommand", prescriptionCommand);
        model.addAttribute("listPrescriptionsSEHR", listPrescriptionsSEHR);
        model.addAttribute("currentPageSEHR", pageNoSEHR);
        model.addAttribute("totalPagesSEHR", prescriptionCommand.getPageInfoCommand().getTotalPages());
        model.addAttribute("totalItemsSEHR", prescriptionCommand.getPageInfoCommand().getTotalElements());
        model.addAttribute("prescriptionService", this.prescriptionService.getCurrentD2DConnection());
        model.addAttribute("isFiltered", this.prescriptionService.isFiltered());
        model.addAttribute("isEmpty", this.prescriptionService.isEmpty());

        return TemplateNames.CURRENT_PATIENT_CURRENT_MEDICATIONS_PRESCRIPTION_VIEW_SECTION;
    }

    @GetMapping
    @RequestMapping("/refresh-data")
    public String refreshData() {
        this.prescriptionService.refreshData();
        return "redirect:/current-patient/current-medications/prescription/view-section";
    }

    @GetMapping
    @RequestMapping("/refresh")
    public String refresh() {
        this.prescriptionService.getPrescriptionsTests();
        return "redirect:/current-patient/current-medications/prescription/view-section";
    }

    @GetMapping
    @RequestMapping("/open-update")
    public String openEditPrescriptionFromSEHR(@RequestParam("id") String id, Model model) {
        model.addAttribute("prescriptionTypes", this.prescriptionService.getPrescriptionTypesRepository().findAll());
        model.addAttribute("prescriptionInfoCommandUpdate", this.prescriptionService.retrievePrescriptionFromSEHRById(id));
        return TemplateNames.CURRENT_PATIENT_CURRENT_MEDICATIONS_PRESCRIPTION_UPDATE_SEHR_PAGE;
    }

    @PutMapping
    @RequestMapping("/update")
    public String updatePrescriptionFromSEHR(@Valid @ModelAttribute("prescriptionInfoCommandUpdate") PrescriptionInfoCommand prescriptionInfoCommand, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("prescriptionTypes", this.prescriptionService.getPrescriptionTypesRepository().findAll());
            return TemplateNames.CURRENT_PATIENT_CURRENT_MEDICATIONS_PRESCRIPTION_UPDATE_SEHR_PAGE;
        }
        this.prescriptionService.updatePrescriptionFromSEHR(prescriptionInfoCommand);
        return "redirect:/current-patient/current-medications/prescription/view-section";
    }
}
