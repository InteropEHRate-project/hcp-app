package eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityPrescription;
import eu.interopehrate.hcpapp.converters.fhir.medicationsummary.HapiToCommandPrescription;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.PrescriptionRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.MedicationSummaryPrescriptionService;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final List<MedicationSummaryPrescriptionInfoCommand> medicationSummaryPrescriptionInfoCommandList = new ArrayList<>();
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private HealthCareProfessionalService healthCareProfessionalService;

    public MedicationSummaryPrescriptionServiceImpl(CurrentPatient currentPatient, HapiToCommandPrescription hapiToCommandPrescription) {
        this.currentPatient = currentPatient;
        this.hapiToCommandPrescription = hapiToCommandPrescription;
    }

    @Override
    public List<MedicationSummaryPrescriptionInfoCommand> getMedicationSummaryPrescriptionInfoCommandList() {
        return medicationSummaryPrescriptionInfoCommandList;
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

    @Override
    public MedicationSummaryPrescriptionCommand prescriptionCommandUpload() {
        return MedicationSummaryPrescriptionCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .medicationSummaryPrescriptionInfoCommand(this.medicationSummaryPrescriptionInfoCommandList)
                .build();
    }

    @Override
    public MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoById(String id) {
        return medicationSummaryPrescriptionInfoCommandList.stream()
                .filter(prescription -> prescription.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("id not found"));
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
        medicationSummaryPrescriptionInfoCommand.setTimings("Frequency: " + medicationSummaryPrescriptionInfoCommand.getFrequency() + "<br/>"
                + "Period: " + medicationSummaryPrescriptionInfoCommand.getPeriod() + "<br/>"
                + "Period unit: " + medicationSummaryPrescriptionInfoCommand.getPeriodUnit());
        this.medicationSummaryPrescriptionInfoCommandList.add(medicationSummaryPrescriptionInfoCommand);
        toSortMethod(this.medicationSummaryPrescriptionInfoCommandList);

        CommandToEntityPrescription commandToEntityPrescription = new CommandToEntityPrescription(healthCareProfessionalService);
        this.prescriptionRepository.save(commandToEntityPrescription.convert(medicationSummaryPrescriptionInfoCommand));
    }

    @Override
    public void deletePrescription(String drugId) {
        for (int i = 0; i < this.medicationSummaryPrescriptionInfoCommandList.size(); i++) {
            if (this.medicationSummaryPrescriptionInfoCommandList.get(i).getId().equalsIgnoreCase(drugId)) {
                this.medicationSummaryPrescriptionInfoCommandList.remove(i);
                break;
            }
        }
    }

    @Override
    public void updatePrescription(MedicationSummaryPrescriptionInfoCommand prescriptionInfoCommand) {
        MedicationSummaryPrescriptionInfoCommand oldPrescription = this.medicationSummaryPrescriptionInfoById(prescriptionInfoCommand.getId());
        oldPrescription.setDrugName(prescriptionInfoCommand.getDrugName());
        oldPrescription.setDateOfPrescription(prescriptionInfoCommand.getDateOfPrescription());
        oldPrescription.setDrugDosage(prescriptionInfoCommand.getDrugDosage());
        oldPrescription.setNotes(prescriptionInfoCommand.getNotes());
        oldPrescription.setStatus(prescriptionInfoCommand.getStatus());
        oldPrescription.setTimings("Frequency: " + prescriptionInfoCommand.getFrequency() + "<br/>"
                + "Period: " + prescriptionInfoCommand.getPeriod() + "<br/>"
                + "Period unit: " + prescriptionInfoCommand.getPeriodUnit());
        oldPrescription.setAuthor(prescriptionInfoCommand.getAuthor());
        toSortMethod(this.medicationSummaryPrescriptionInfoCommandList);
    }

    private static void toSortMethod(List<MedicationSummaryPrescriptionInfoCommand> med) {
        med.sort((o1, o2) -> {
            if (o1.getStatus().equalsIgnoreCase("Active") && o2.getStatus().equalsIgnoreCase("Suspended")) {
                return -1;
            }
            if (o1.getStatus().equalsIgnoreCase("Active") && o2.getStatus().equalsIgnoreCase("Stopped")) {
                return -1;
            }
            if (o1.getStatus().equalsIgnoreCase("Suspended") && o2.getStatus().equalsIgnoreCase("Stopped")) {
                return -1;
            }
            if (o1.getStatus().equalsIgnoreCase("Stopped") && o2.getStatus().equalsIgnoreCase("Suspended")) {
                return 1;
            }
            if (o1.getStatus().equalsIgnoreCase("Stopped") && o2.getStatus().equalsIgnoreCase("Active")) {
                return 1;
            }
            if (o1.getStatus().equalsIgnoreCase("Suspended") && o2.getStatus().equalsIgnoreCase("Active")) {
                return 1;
            }
            return 0;
        });
    }
}
