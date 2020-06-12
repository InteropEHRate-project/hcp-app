package eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.converters.fhir.medicationsummary.HapiToCommandPrescription;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryPrescriptionService;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class MedicationSummaryPrescriptionServiceImpl implements MedicationSummaryPrescriptionService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandPrescription hapiToCommandPrescription;
    private List<MedicationSummaryPrescriptionInfoCommand> medicationSummaryPrescriptionInfoCommandList = new ArrayList<>();

    public MedicationSummaryPrescriptionServiceImpl(CurrentPatient currentPatient, HapiToCommandPrescription hapiToCommandPrescription) {
        this.currentPatient = currentPatient;
        this.hapiToCommandPrescription = hapiToCommandPrescription;
    }

    @Override
    public MedicationSummaryPrescriptionCommand prescriptionCommand() throws IOException {
        List<MedicationSummaryPrescriptionInfoCommand> medicationSummaryPrescriptionInfoCommandList = new ArrayList<>();

        if (Objects.isNull(this.currentPatient.getPrescription())) {
            File json = new ClassPathResource("MedicationRequest-PRESCRIPTION-sample.json").getFile();
            FileInputStream file = new FileInputStream(json);
            String lineReadtest = readFromInputStream(file);
            IParser parser = FhirContext.forR4().newJsonParser();
            MedicationRequest medicationRequest = parser.parseResource(MedicationRequest.class, lineReadtest);

            medicationSummaryPrescriptionInfoCommandList.add(this.hapiToCommandPrescription.convert(medicationRequest));
            log.info("On plain JSON Prescription");
            return MedicationSummaryPrescriptionCommand.builder()
                    .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                    .medicationSummaryPrescriptionInfoCommand(medicationSummaryPrescriptionInfoCommandList)
                    .build();
        }
        medicationSummaryPrescriptionInfoCommandList.add(this.hapiToCommandPrescription.convert(this.currentPatient.getPrescription()));
        log.info("On S-EHR Prescription received");
        return MedicationSummaryPrescriptionCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .medicationSummaryPrescriptionInfoCommand(medicationSummaryPrescriptionInfoCommandList)
                .build();
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    @Override
    public void insertPrescription(MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand) {
        this.medicationSummaryPrescriptionInfoCommandList.add(medicationSummaryPrescriptionInfoCommand);
    }
}
