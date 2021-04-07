package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.PermanentStorageOfCloudDataService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/permanent-storage")
public class PermanentStorageOfCloudDataController {
    private final PermanentStorageOfCloudDataService permanentStorageOfCloudDataService;

    public PermanentStorageOfCloudDataController(PermanentStorageOfCloudDataService permanentStorageOfCloudDataService) {
        this.permanentStorageOfCloudDataService = permanentStorageOfCloudDataService;
    }

    @GetMapping
    @RequestMapping("/view-section")
    public String view() {
        return TemplateNames.CURRENT_PATIENT_PERMANENT_STORAGE;
    }

    @GetMapping
    @RequestMapping("/store")
    public String storeData(Model model) {
        this.permanentStorageOfCloudDataService.storePatientData();
        model.addAttribute("sendSuccessfully", Boolean.TRUE);
        return TemplateNames.CURRENT_PATIENT_PERMANENT_STORAGE;
    }
}
