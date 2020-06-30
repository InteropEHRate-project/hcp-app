package eu.interopehrate.hcpapp.services.currentpatient.impl.medicationsummary;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityPrescription;
import eu.interopehrate.hcpapp.converters.fhir.medicationsummary.HapiToCommandPrescription;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;
import eu.interopehrate.hcpapp.jpa.repositories.PrescriptionRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.PrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.PrescriptionInfoCommand;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.medicationsummary.PrescriptionService;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Timing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandPrescription hapiToCommandPrescription;
    private List<PrescriptionInfoCommand> prescriptionInfoCommands = new ArrayList<>();
    private CommandToEntityPrescription commandToEntityPrescription = new CommandToEntityPrescription();
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private HealthCareProfessionalService healthCareProfessionalService;
    private CurrentD2DConnection currentD2DConnection;

    public PrescriptionServiceImpl(CurrentPatient currentPatient, HapiToCommandPrescription hapiToCommandPrescription, CurrentD2DConnection currentD2DConnection) {
        this.currentPatient = currentPatient;
        this.hapiToCommandPrescription = hapiToCommandPrescription;
        this.currentD2DConnection = currentD2DConnection;
    }

    @Override
    public PrescriptionCommand prescriptionCommand() throws IOException {
        List<PrescriptionInfoCommand> prescriptionInfoCommandList = new ArrayList<>();

        if (Objects.isNull(this.currentPatient.getPrescription())) {
            File json = new ClassPathResource("MedicationRequest-PRESCRIPTION-sample.json").getFile();
            FileInputStream file = new FileInputStream(json);
            String lineReadtest = readFromInputStream(file);
            IParser parser = FhirContext.forR4().newJsonParser();
            MedicationRequest medicationRequest = parser.parseResource(MedicationRequest.class, lineReadtest);
            PrescriptionInfoCommand prescriptionInfoCommand = this.hapiToCommandPrescription.convert(medicationRequest);

            prescriptionInfoCommandList.add(prescriptionInfoCommand);
            log.info("On plain JSON Prescription");
            return PrescriptionCommand.builder()
                    .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                    .prescriptionInfoCommand(prescriptionInfoCommandList)
                    .build();
        }

        if (Objects.nonNull(this.currentPatient.getPrescription())) {
            PrescriptionInfoCommand prescriptionInfoCommand = this.hapiToCommandPrescription.convert(this.currentPatient.getPrescription());
            prescriptionInfoCommandList.add(prescriptionInfoCommand);
        }
        return PrescriptionCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .prescriptionInfoCommand(prescriptionInfoCommandList)
                .build();
    }

    @Override
    public PrescriptionCommand prescriptionCommandUpload() {
        return PrescriptionCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .prescriptionInfoCommand(this.prescriptionInfoCommands)
                .build();
    }

    @Override
    public PrescriptionInfoCommand prescriptionInfoCommandById(Long id) {
        return this.prescriptionInfoCommands
                .stream()
                .filter(prescription -> prescription.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("id not found"));
    }

    @Override
    public void insertPrescription(PrescriptionInfoCommand prescriptionInfoCommand) {
        prescriptionInfoCommand.setTimings(prescriptionInfoCommand.getFrequency() + " times per "
                + prescriptionInfoCommand.getPeriod() + " "
                + prescriptionInfoCommand.getPeriodUnit());

        PrescriptionEntity prescriptionEntity = this.commandToEntityPrescription.convert(prescriptionInfoCommand);
        prescriptionEntity.setAuthor(healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " " + healthCareProfessionalService.getHealthCareProfessional().getLastName());
        prescriptionInfoCommand.setAuthor(healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " " + healthCareProfessionalService.getHealthCareProfessional().getLastName());

        prescriptionEntity.setPeriodUnit(toShortUnit(prescriptionEntity.getPeriodUnit()));
        this.prescriptionRepository.save(prescriptionEntity);
        //Adding the ID from the database to the InfoCommand
        prescriptionInfoCommand.setId(prescriptionEntity.getId());
        this.prescriptionInfoCommands.add(prescriptionInfoCommand);
        this.prescriptionInfoCommands = toSortMethodCommand(this.prescriptionInfoCommands);
    }

    @Override
    public void deletePrescription(Long drugId) {
        this.prescriptionInfoCommands.removeIf(pre -> pre.getId().equals(drugId));
        this.prescriptionRepository.deleteById(drugId);
    }

    @Override
    public void updatePrescription(PrescriptionInfoCommand prescriptionInfoCommand) {
        PrescriptionInfoCommand oldPrescription = prescriptionInfoCommandById(prescriptionInfoCommand.getId());
        oldPrescription.setDrugName(prescriptionInfoCommand.getDrugName());
        oldPrescription.setDateOfPrescription(prescriptionInfoCommand.getDateOfPrescription());
        oldPrescription.setDrugDosage(prescriptionInfoCommand.getDrugDosage());
        oldPrescription.setNotes(prescriptionInfoCommand.getNotes());
        oldPrescription.setStatus(prescriptionInfoCommand.getStatus());
        oldPrescription.setFrequency(prescriptionInfoCommand.getFrequency());
        oldPrescription.setPeriod(prescriptionInfoCommand.getPeriod());
        oldPrescription.setPeriodUnit(prescriptionInfoCommand.getPeriodUnit());
        oldPrescription.setTimings(prescriptionInfoCommand.getFrequency() + " times per "
                + prescriptionInfoCommand.getPeriod() + " "
                + prescriptionInfoCommand.getPeriodUnit());
        oldPrescription.setStart(prescriptionInfoCommand.getStart());
        oldPrescription.setEnd(prescriptionInfoCommand.getEnd());

        PrescriptionEntity prescriptionEntity = this.prescriptionRepository.getOne(prescriptionInfoCommand.getId());
        prescriptionEntity.setDrugName(prescriptionInfoCommand.getDrugName());
        prescriptionEntity.setDateOfPrescription(prescriptionInfoCommand.getDateOfPrescription());
        prescriptionEntity.setDrugDosage(prescriptionInfoCommand.getDrugDosage());
        prescriptionEntity.setNotes(prescriptionInfoCommand.getNotes());
        prescriptionEntity.setStatus(prescriptionInfoCommand.getStatus());
        prescriptionEntity.setFrequency(prescriptionInfoCommand.getFrequency());
        prescriptionEntity.setPeriod(prescriptionInfoCommand.getPeriod());
        prescriptionEntity.setPeriodUnit(toShortUnit(prescriptionInfoCommand.getPeriodUnit()));
        prescriptionEntity.setTimings(oldPrescription.getTimings());
        prescriptionEntity.setStart(prescriptionInfoCommand.getStart());
        prescriptionEntity.setEnd(prescriptionInfoCommand.getEnd());
        this.prescriptionRepository.save(prescriptionEntity);

        this.prescriptionInfoCommands = toSortMethodCommand(this.prescriptionInfoCommands);
    }

    @Override
    public void callSendPrescription() throws IOException {
        for (int i = 0; i < this.prescriptionRepository.findAll().size(); i++) {
            MedicationRequest med = createPrescriptionFromEntity(this.prescriptionRepository.findAll().get(i));
            this.sendPrescription(med);
        }
    }

    @Override
    public void sendPrescription(MedicationRequest medicationRequest) throws IOException {
        this.currentD2DConnection.getConnectedThread().sendPrescription(medicationRequest);
        log.info("Prescription sent to S-EHR");
    }

    private static List<PrescriptionInfoCommand> toSortMethodCommand(List<PrescriptionInfoCommand> med) {
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
        return med;
    }

    private static MedicationRequest createPrescriptionFromEntity (PrescriptionEntity prescriptionEntity) {
        MedicationRequest medicationRequest = new MedicationRequest();

        medicationRequest.setMedication(new CodeableConcept().setText(prescriptionEntity.getDrugName()));

        Timing t = new Timing();
        t.getRepeat().setFrequency(prescriptionEntity.getFrequency());
        t.getRepeat().setPeriod(prescriptionEntity.getPeriod());
        t.getRepeat().setPeriodUnit(Timing.UnitsOfTime.fromCode(prescriptionEntity.getPeriodUnit()));
        List<Dosage> d = new ArrayList<>();
        d.add(new Dosage().setTiming(t));

        medicationRequest.getDosageInstructionFirstRep().getTiming().getRepeat().addChild("boundsPeriod");
        Date dateStart = Date.from(prescriptionEntity.getStart().atStartOfDay(ZoneId.systemDefault()).toInstant());
        medicationRequest.getDosageInstructionFirstRep().getTiming().getRepeat().getBoundsPeriod().setStart(dateStart);
        Date dateEnd = Date.from(prescriptionEntity.getEnd().atStartOfDay(ZoneId.systemDefault()).toInstant());
        medicationRequest.getDosageInstructionFirstRep().getTiming().getRepeat().getBoundsPeriod().setEnd(dateEnd);

        medicationRequest.setDosageInstruction(d);

        medicationRequest.setStatus(MedicationRequest.MedicationRequestStatus.fromCode(prescriptionEntity.getStatus().toLowerCase()));

        medicationRequest.setAuthoredOn(Date.from(prescriptionEntity.getDateOfPrescription().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        medicationRequest.setId(prescriptionEntity.getId().toString());

        List<Dosage> d2 = new ArrayList<>();
        d2.add(new Dosage().setText(prescriptionEntity.getNotes()));
        medicationRequest.setDosageInstruction(d2);

        return medicationRequest;
    }

    private static String toShortUnit(String unit) {
        String result;
        switch (unit) {
            case "minute":
                result = "min";
                break;
            case "hour":
                result = "h";
                break;
            case "day":
                result = "d";
                break;
            case "week":
                result = "wk";
                break;
            case "month":
                result = "mo";
                break;
            case "year":
                result = "a";
                break;
            default:
                result = unit;
                break;
        }
        return result;
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
}
