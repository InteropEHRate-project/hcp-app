package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import eu.interopehrate.hcpapp.services.currentpatient.*;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.MedicationService;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import eu.interopehrate.hcpapp.services.currentpatient.laboratorytests.ObservationLaboratoryService;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OutpatientReportCommand {
    private final PrescriptionService prescriptionService;
    private final VitalSignsService vitalSignsService;
    private final MedicationService medicationService;
    private final CurrentDiseaseService currentDiseaseService;
    private final AllergyService allergyService;
    private final DiagnosticConclusionService diagnosticConclusionService;
    private final InstrumentsExaminationService instrumentsExaminationService;
    private final PHExamService phExamService;
    private final ObservationLaboratoryService observationLaboratoryService;
}
