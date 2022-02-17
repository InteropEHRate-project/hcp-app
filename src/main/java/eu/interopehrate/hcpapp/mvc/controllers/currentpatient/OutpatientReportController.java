package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.OutpatientReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/current-patient/outpatient-report")
public class OutpatientReportController {
    private final OutpatientReportService outpatientReportService;

    public OutpatientReportController(OutpatientReportService outpatientReportService) {
        this.outpatientReportService = outpatientReportService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection(Model model) {
        model.addAttribute("outpatientReportCommand", this.outpatientReportService.outpatientReportCommand());
        return TemplateNames.CURRENT_PATIENT_OUTPATIENT_REPORT;
    }

    @GetMapping
    @RequestMapping("/idToMedication")
    public String viewSection(@RequestParam(name = "id") String id, Model model) {
        model.addAttribute("prescription", this.outpatientReportService.outpatientReportCommand().getMedicationService().visitFind(Long.parseLong(id)));
        model.addAttribute("translation", this.outpatientReportService.outpatientReportCommand().getMedicationService().getCurrentPatient().getDisplayTranslatedVersion());
        return TemplateNames.CURRENT_PATIENT_OUTPATIENT_REPORT_MEDICATION_VIEW;
    }

    @GetMapping
    @RequestMapping("/sendToSehr")
    public String sendToSehr(Model model) throws IOException {
        if (!this.outpatientReportService.outpatientReportCommand().getPrescriptionService().getPrescriptionRepository().findAll().isEmpty() &&
                Objects.nonNull(this.outpatientReportService.outpatientReportCommand().getPrescriptionService().getCurrentD2DConnection().getTd2D())) {
            this.outpatientReportService.outpatientReportCommand().getPrescriptionService().callSendPrescription();
        }
        if (!this.outpatientReportService.outpatientReportCommand().getVitalSignsService().vitalSignsUpload().getVitalSignsInfoCommands().isEmpty() &&
                Objects.nonNull(this.outpatientReportService.outpatientReportCommand().getVitalSignsService().getCurrentD2DConnection().getTd2D())) {
            this.outpatientReportService.outpatientReportCommand().getVitalSignsService().callVitalSigns();
        }
        if (!this.outpatientReportService.outpatientReportCommand().getCurrentDiseaseService().currentDiseasesSection().getCurrentDiseaseInfoCommand().isEmpty()
                && Objects.nonNull(this.outpatientReportService.outpatientReportCommand().getCurrentDiseaseService().getCurrentD2DConnection().getTd2D())) {
            this.outpatientReportService.outpatientReportCommand().getCurrentDiseaseService().callSendCurrentDiseases();
        }
//        if (!this.outpatientReportService.outpatientReportCommand().getConclusionService().conclusionComm().getConclusionInfoCommandList().isEmpty()
//                && Objects.nonNull(this.outpatientReportService.outpatientReportCommand().getConclusionService().getCurrentD2DConnection().getTd2D())) {
//            this.outpatientReportService.outpatientReportCommand().getConclusionService().callSendConclusion();
//        }
        model.addAttribute("dataSent", Boolean.TRUE);
        return this.viewSection(model);
    }
}
