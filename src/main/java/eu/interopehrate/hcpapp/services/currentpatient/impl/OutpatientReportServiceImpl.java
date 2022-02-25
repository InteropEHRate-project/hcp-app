package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.OutpatientReportCommand;
import eu.interopehrate.hcpapp.services.currentpatient.*;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.MedicationService;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import org.springframework.stereotype.Service;

@Service
public class OutpatientReportServiceImpl implements OutpatientReportService {
    private final PrescriptionService prescriptionService;
    private final VitalSignsService vitalSignsService;
    private final MedicationService medicationService;
    private final CurrentDiseaseService currentDiseaseService;
    private final AllergyService allergyService;
    private final DiagnosticConclusionService diagnosticConclusionService;
    private final InstrumentsExaminationService instrumentsExaminationService;

    public OutpatientReportServiceImpl(PrescriptionService prescriptionService, VitalSignsService vitalSignsService,
                                       MedicationService medicationService, CurrentDiseaseService currentDiseaseService,
                                       AllergyService allergyService, DiagnosticConclusionService diagnosticConclusionService, InstrumentsExaminationService instrumentsExaminationService) {
        this.prescriptionService = prescriptionService;
        this.vitalSignsService = vitalSignsService;
        this.medicationService = medicationService;
        this.currentDiseaseService = currentDiseaseService;
        this.allergyService = allergyService;
        this.diagnosticConclusionService = diagnosticConclusionService;
        this.instrumentsExaminationService = instrumentsExaminationService;
    }

    @Override
    public OutpatientReportCommand outpatientReportCommand() {
        return OutpatientReportCommand.builder()
                .prescriptionService(this.prescriptionService)
                .vitalSignsService(this.vitalSignsService)
                .medicationService(this.medicationService)
                .currentDiseaseService(this.currentDiseaseService)
                .allergyService(this.allergyService)
                .diagnosticConclusionService(this.diagnosticConclusionService)
                .instrumentsExaminationService(this.instrumentsExaminationService)
                .build();
    }
}
