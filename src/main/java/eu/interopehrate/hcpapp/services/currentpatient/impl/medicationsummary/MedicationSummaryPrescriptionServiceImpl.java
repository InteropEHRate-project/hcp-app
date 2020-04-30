package eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryPrescriptionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MedicationSummaryPrescriptionServiceImpl implements MedicationSummaryPrescriptionService {
    private final CurrentPatient currentPatient;
    private List<MedicationSummaryPrescriptionInfoCommand> medicationSummaryPrescriptionInfoCommandList = new ArrayList<>();
    //Hardcoded records in Prescription tabel
    private MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand1 = new MedicationSummaryPrescriptionInfoCommand();
    private MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand2 = new MedicationSummaryPrescriptionInfoCommand();
    private MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand3 = new MedicationSummaryPrescriptionInfoCommand();

    public MedicationSummaryPrescriptionServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public MedicationSummaryPrescriptionCommand prescriptionCommand(String id) {
        List<MedicationSummaryPrescriptionInfoCommand> medicationSummaryPrescriptionInfoCommandList = new ArrayList<>();
        medicationSummaryPrescriptionInfoCommandList.add(medicationSummaryPrescriptionInfoCommand1);
        medicationSummaryPrescriptionInfoCommandList.add(medicationSummaryPrescriptionInfoCommand2);
        medicationSummaryPrescriptionInfoCommandList.add(medicationSummaryPrescriptionInfoCommand3);
        return MedicationSummaryPrescriptionCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .medicationSummaryPrescriptionInfoCommandList(medicationSummaryPrescriptionInfoCommandList)
                .build();
    }

    @Override
    public void insertPrescription(MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand) {
        this.medicationSummaryPrescriptionInfoCommandList.add(medicationSummaryPrescriptionInfoCommand);
    }
}
