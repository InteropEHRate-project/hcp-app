package eu.interopehrate.hcpapp.services.currentpatient.impl.currentmedications;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.MedicationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.MedicationInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.MedicationService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MedicationServiceImpl implements MedicationService {
    private final CurrentPatient currentPatient;

    public MedicationServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public MedicationCommand medicationCommand(String id, String drug, String status, String notes, String timings, String drugDosage, LocalDate dateOfPrescription, LocalDate start, LocalDate end) {
        MedicationInfoCommand medicationInfoCommand = new MedicationInfoCommand();
        medicationInfoCommand.setId(id);
        medicationInfoCommand.setDrugName(drug);
        medicationInfoCommand.setStatus(status);
        medicationInfoCommand.setNotes(notes);
        medicationInfoCommand.setTimings(timings);
        medicationInfoCommand.setDrugDosage(drugDosage);
        medicationInfoCommand.setDateOfPrescription(dateOfPrescription);
        medicationInfoCommand.setStart(start);
        medicationInfoCommand.setEnd(end);
        return MedicationCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .medicationInfoCommand(medicationInfoCommand)
                .build();
    }
}
