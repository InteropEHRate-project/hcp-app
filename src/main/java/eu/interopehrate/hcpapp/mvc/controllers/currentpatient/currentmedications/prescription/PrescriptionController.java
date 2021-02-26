package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.currentmedications.prescription;

import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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
        this.prescriptionService.setEmpty(false);
        this.prescriptionService.setFiltered(false);
        session.setAttribute("keywordPrescription", keywordPrescription);
        return this.findPaginated(1, 1, keywordPrescription, model);
    }

    @GetMapping
    @RequestMapping("/open-add-page")
    public String openAddPage(Model model) {
        model.addAttribute("prescriptionInfoCommand", new PrescriptionInfoCommand());
        return TemplateNames.CURRENT_PATIENT_PRESCRIPTION_ADD_PAGE;
    }

    @GetMapping
    @RequestMapping("/view-section/page/{pageNo}/{pageNoSEHR}/keywordPrescription/{keywordPrescription}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @PathVariable(value = "pageNoSEHR") int pageNoSEHR,
                                @PathVariable(value = "keywordPrescription") String keywordPrescription,
                                Model model) throws IOException {
        //PAGE SIZE is hardcoded HERE
        int pageSize = 3;
        Page<PrescriptionEntity> page = this.prescriptionService.findPaginated(pageNo, pageSize);
        PrescriptionController.prescriptionEntityList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listPrescriptions", prescriptionEntityList);

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

    @PostMapping
    @RequestMapping("/save-add")
    public String saveAdd(@Valid @ModelAttribute PrescriptionInfoCommand prescriptionInfoCommand, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return TemplateNames.CURRENT_PATIENT_PRESCRIPTION_ADD_PAGE;
        }
        prescriptionService.insertPrescription(prescriptionInfoCommand);
        return "redirect:/current-patient/current-medications/prescription/view-section";
    }

    @DeleteMapping
    @RequestMapping("/delete")
    public String delete(@RequestParam("drugId") Long drugId) {
        this.prescriptionService.deletePrescription(drugId);
        return "redirect:/current-patient/current-medications/prescription/view-section";
    }

    @GetMapping
    @RequestMapping("/open-update-page")
    public String openUpdatePage(@RequestParam("id") Long id, Model model) {
        model.addAttribute("prescriptionInfoCommand", this.prescriptionService.prescriptionInfoCommandById(id));
        return TemplateNames.CURRENT_PATIENT_PRESCRIPTION_UPDATE_PAGE;
    }

    @PostMapping
    @RequestMapping("/update")
    public String update(@Valid @ModelAttribute PrescriptionInfoCommand prescriptionInfoCommand, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("prescriptionInfoCommand", prescriptionInfoCommand);
            return TemplateNames.CURRENT_PATIENT_PRESCRIPTION_UPDATE_PAGE;
        }
        this.prescriptionService.updatePrescription(prescriptionInfoCommand);
        return "redirect:/current-patient/current-medications/prescription/view-section";
    }
}
