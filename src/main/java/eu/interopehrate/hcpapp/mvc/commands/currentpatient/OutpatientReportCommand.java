package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import eu.interopehrate.hcpapp.services.currentpatient.*;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.MedicationService;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import eu.interopehrate.hcpapp.services.currentpatient.laboratorytests.ObservationLaboratoryService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

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
    private final String hcpName;
    private final String hospitalName;
    private final String hospitalAddress;
    private final String patientName;
    private final String patientSex;
    private final Date patientBirthDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private final LocalDateTime dateOfVisit;
    private final String format;
}
