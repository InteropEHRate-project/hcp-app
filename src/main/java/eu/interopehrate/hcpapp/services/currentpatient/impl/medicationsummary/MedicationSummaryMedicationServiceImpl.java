package eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryMedicationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryMedicationInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryMedicationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicationSummaryMedicationServiceImpl implements MedicationSummaryMedicationService {
    private final CurrentPatient currentPatient;
    //Hardcoded Medication
    private MedicationSummaryMedicationInfoCommand medicationSummaryMedicationInfoCommand = new MedicationSummaryMedicationInfoCommand();

    public MedicationSummaryMedicationServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public MedicationSummaryMedicationCommand medicationCommand(String id) {
        List<MedicationSummaryMedicationInfoCommand> medicationSummaryMedicationInfoCommands = new ArrayList<>();
        this.medicationSummaryMedicationInfoCommand.setId(id);
        medicationSummaryMedicationInfoCommands.add(medicationSummaryMedicationInfoCommand);
        return MedicationSummaryMedicationCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .medicationSummaryMedicationInfoCommandList(medicationSummaryMedicationInfoCommands)
                .build();
    }
}
