package eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryMedicationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryMedicationInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryMedicationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MedicationSummaryMedicationServiceImpl implements MedicationSummaryMedicationService {
    private final CurrentPatient currentPatient;
    private MedicationSummaryMedicationInfoCommand medicationSummaryMedicationInfoCommand = new MedicationSummaryMedicationInfoCommand();

    public MedicationSummaryMedicationServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public MedicationSummaryMedicationCommand medicationCommand(String id, String drug, String status, String notes, String timings, String drugDosage, LocalDate dateOfPrescription, LocalDate start, LocalDate end) {
        List<MedicationSummaryMedicationInfoCommand> medicationSummaryMedicationInfoCommands = new ArrayList<>();
        this.medicationSummaryMedicationInfoCommand.setId(id);
        this.medicationSummaryMedicationInfoCommand.setDrugName(drug);
        this.medicationSummaryMedicationInfoCommand.setStatus(status);
        this.medicationSummaryMedicationInfoCommand.setNotes(notes);
        this.medicationSummaryMedicationInfoCommand.setTimings(timings);
        this.medicationSummaryMedicationInfoCommand.setDrugDosage(drugDosage);
        this.medicationSummaryMedicationInfoCommand.setDateOfPrescription(dateOfPrescription);
        this.medicationSummaryMedicationInfoCommand.setStart(start);
        this.medicationSummaryMedicationInfoCommand.setEnd(end);
        medicationSummaryMedicationInfoCommands.add(medicationSummaryMedicationInfoCommand);
        return MedicationSummaryMedicationCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .medicationSummaryMedicationInfoCommandList(medicationSummaryMedicationInfoCommands)
                .build();
    }
}
