package eu.interopehrate.hcpapp.services.currentpatient.impl.currentmedications;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.MedicationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.MedicationInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.MedicationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MedicationServiceImpl implements MedicationService {
    private final CurrentPatient currentPatient;
    private MedicationInfoCommand medicationInfoCommand = new MedicationInfoCommand();

    public MedicationServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public MedicationCommand medicationCommand(String id, String drug, String status, String notes, String timings, String drugDosage, LocalDate dateOfPrescription, LocalDate start, LocalDate end) {
        List<MedicationInfoCommand> medicationInfoCommands = new ArrayList<>();
        this.medicationInfoCommand.setId(id);
        this.medicationInfoCommand.setDrugName(drug);
        this.medicationInfoCommand.setStatus(status);
        this.medicationInfoCommand.setNotes(notes);
        this.medicationInfoCommand.setTimings(timings);
        this.medicationInfoCommand.setDrugDosage(drugDosage);
        this.medicationInfoCommand.setDateOfPrescription(dateOfPrescription);
        this.medicationInfoCommand.setStart(start);
        this.medicationInfoCommand.setEnd(end);
        medicationInfoCommands.add(medicationInfoCommand);
        return MedicationCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .medicationInfoCommandList(medicationInfoCommands)
                .build();
    }
}
