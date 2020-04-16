package eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryCommand;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryPrescriptionService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MedicationSummaryPrescriptionServiceImpl implements MedicationSummaryPrescriptionService {
    private final CurrentPatient currentPatient;

    public MedicationSummaryPrescriptionServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public MedicationSummaryCommand prescriptionCommand() {
        return MedicationSummaryCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .medicationList(Collections.emptyList())
                .build();
    }
}
