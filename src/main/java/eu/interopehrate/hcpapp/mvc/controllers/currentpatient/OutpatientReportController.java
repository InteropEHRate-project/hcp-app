package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.VitalSignsService;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/current-patient/outpatient-report")
public class OutpatientReportController {
    private final PrescriptionService prescriptionService;
    private final VitalSignsService vitalSignsService;

    public OutpatientReportController(PrescriptionService prescriptionService, VitalSignsService vitalSignsService) {
        this.prescriptionService = prescriptionService;
        this.vitalSignsService = vitalSignsService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("listPrescriptions", this.prescriptionService.getPrescriptionRepository().findAll());
        model.addAttribute("prescriptionService", this.prescriptionService.getCurrentD2DConnection());
        model.addAttribute("vitalSignsUpload", this.vitalSignsService.vitalSignsUpload());
        model.addAttribute("vitalSignsService", this.vitalSignsService.getCurrentD2DConnection());
        return TemplateNames.CURRENT_PATIENT_OUTPATIENT_REPORT;
    }

    @GetMapping
    @RequestMapping("/sendToSehr")
    public String sendToSehr(Model model) throws IOException {
        if (!this.prescriptionService.getPrescriptionRepository().findAll().isEmpty() && Objects.nonNull(this.prescriptionService.getCurrentD2DConnection().getConnectedThread())) {
            this.prescriptionService.callSendPrescription();
        }
        if (!this.vitalSignsService.vitalSignsUpload().getVitalSignsInfoCommands().isEmpty() && Objects.nonNull(this.vitalSignsService.getCurrentD2DConnection().getConnectedThread())) {
            this.vitalSignsService.callVitalSigns();
        }
        model.addAttribute("dataSent", Boolean.TRUE);
        model.addAttribute("listPrescriptions", this.prescriptionService.getPrescriptionRepository().findAll());
        model.addAttribute("prescriptionService", this.prescriptionService.getCurrentD2DConnection());
        model.addAttribute("vitalSignsUpload", this.vitalSignsService.vitalSignsUpload());
        model.addAttribute("vitalSignsService", this.vitalSignsService.getCurrentD2DConnection());
        return TemplateNames.CURRENT_PATIENT_OUTPATIENT_REPORT;
    }
}
