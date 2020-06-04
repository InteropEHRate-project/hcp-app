package eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary;

import eu.interopehrate.hcpapp.converters.fhir.medicationsummary.HapiToCommandPrescription;
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
    private final HapiToCommandPrescription hapiToCommandPrescription;
    private List<MedicationSummaryPrescriptionInfoCommand> medicationSummaryPrescriptionInfoCommandList = new ArrayList<>();
    //Hardcoded records in Prescription tabel
    private MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand1 = new MedicationSummaryPrescriptionInfoCommand();
    private MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand2 = new MedicationSummaryPrescriptionInfoCommand();
    private MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand3 = new MedicationSummaryPrescriptionInfoCommand();

    public MedicationSummaryPrescriptionServiceImpl(CurrentPatient currentPatient, HapiToCommandPrescription hapiToCommandPrescription) {
        this.currentPatient = currentPatient;
        this.hapiToCommandPrescription = hapiToCommandPrescription;
    }

    @Override
    public MedicationSummaryPrescriptionCommand prescriptionCommand() {
//        List<MedicationSummaryPrescriptionInfoCommand> medicationSummaryPrescriptionInfoCommandList = new ArrayList<>();
//        this.medicationSummaryPrescriptionInfoCommand1.setDrugName("Data test 1");
//        medicationSummaryPrescriptionInfoCommandList.add(medicationSummaryPrescriptionInfoCommand1);
//        this.medicationSummaryPrescriptionInfoCommand2.setDrugName("Data test 2");
//        this.medicationSummaryPrescriptionInfoCommand2.setStatus("suspended");
//        medicationSummaryPrescriptionInfoCommandList.add(medicationSummaryPrescriptionInfoCommand2);
//        this.medicationSummaryPrescriptionInfoCommand3.setDrugName("Data test 3");
//        this.medicationSummaryPrescriptionInfoCommand3.setStatus("stopped");
//        medicationSummaryPrescriptionInfoCommandList.add(medicationSummaryPrescriptionInfoCommand3);
        if (this.currentPatient.getPrescription() == null) {
            return MedicationSummaryPrescriptionCommand.builder()
                    .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion()).build();
        }
        var medicationSummaryPrescriptionInfoCommand = this.hapiToCommandPrescription.convert(currentPatient.getPrescription());
        return MedicationSummaryPrescriptionCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .medicationSummaryPrescriptionInfoCommand(medicationSummaryPrescriptionInfoCommand)
                .build();
    }

    @Override
    public void insertPrescription(MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand) {
        this.medicationSummaryPrescriptionInfoCommandList.add(medicationSummaryPrescriptionInfoCommand);
    }
}
