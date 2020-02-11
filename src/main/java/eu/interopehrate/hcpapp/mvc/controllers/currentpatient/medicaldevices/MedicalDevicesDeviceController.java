package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.medicaldevices;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/current-patient/medical-devices/device")
public class MedicalDevicesDeviceController {
    @GetMapping
    @RequestMapping("/view-section")
    public String viewSection() {
        return TemplateNames.CURRENT_PATIENT_MEDICAL_DEVICES_DEVICE_VIEW_SECTION;
    }
}
