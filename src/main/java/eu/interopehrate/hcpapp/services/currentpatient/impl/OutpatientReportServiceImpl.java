package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.OutpatientReportCommand;
import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import eu.interopehrate.hcpapp.services.currentpatient.OutpatientReportService;
import eu.interopehrate.hcpapp.services.currentpatient.VitalSignsService;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.MedicationService;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import org.springframework.stereotype.Service;

@Service
public class OutpatientReportServiceImpl implements OutpatientReportService {
    private final PrescriptionService prescriptionService;
    private final VitalSignsService vitalSignsService;
    private final MedicationService medicationService;
    private final CurrentDiseaseService currentDiseaseService;

    public OutpatientReportServiceImpl(PrescriptionService prescriptionService, VitalSignsService vitalSignsService,
                                       MedicationService medicationService, CurrentDiseaseService currentDiseaseService) {
        this.prescriptionService = prescriptionService;
        this.vitalSignsService = vitalSignsService;
        this.medicationService = medicationService;
        this.currentDiseaseService = currentDiseaseService;
    }

    @Override
    public OutpatientReportCommand outpatientReportCommand() {
        return OutpatientReportCommand.builder()
                .prescriptionService(this.prescriptionService)
                .vitalSignsService(this.vitalSignsService)
                .medicationService(this.medicationService)
                .currentDiseaseService(this.currentDiseaseService)
                .build();
    }
}
