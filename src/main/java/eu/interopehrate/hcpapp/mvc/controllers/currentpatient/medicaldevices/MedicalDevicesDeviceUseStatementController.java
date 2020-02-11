package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicaldevices;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/medical-devices/use-device-statement")
public class MedicalDevicesDeviceUseStatementController {
    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_MEDICAL_DEVICES_USE_DEVICE_STATEMENT_VIEW_SECTION;
    }
}
