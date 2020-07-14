package eu.interopehrate.hcpapp.services.currentpatient.impl.currentmedications;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityPrescription;
import eu.interopehrate.hcpapp.converters.fhir.currentmedications.HapiToCommandPrescription;
import eu.interopehrate.hcpapp.converters.fhir.currentmedications.HapiToCommandPrescriptionTranslate;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;
import eu.interopehrate.hcpapp.jpa.repositories.PrescriptionRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandPrescription hapiToCommandPrescription;
    private final HapiToCommandPrescriptionTranslate hapiToCommandPrescriptionTranslate;
    private List<PrescriptionInfoCommand> prescriptionInfoCommands = new ArrayList<>();
    private List<PrescriptionInfoCommand> prescriptionsUploadedToSEHR = new ArrayList<>();
    private CommandToEntityPrescription commandToEntityPrescription = new CommandToEntityPrescription();
    @Autowired
    private PrescriptionRepository prescriptionRepository;
    @Autowired
    private HealthCareProfessionalService healthCareProfessionalService;
    private CurrentD2DConnection currentD2DConnection;
    private Bundle prescriptionFromJSON;
    private Bundle prescriptionTranslatedFromJSON;

    public PrescriptionServiceImpl(CurrentPatient currentPatient, HapiToCommandPrescription hapiToCommandPrescription, HapiToCommandPrescriptionTranslate hapiToCommandPrescriptionTranslate, CurrentD2DConnection currentD2DConnection) throws IOException {
        this.currentPatient = currentPatient;
        this.hapiToCommandPrescription = hapiToCommandPrescription;
        this.hapiToCommandPrescriptionTranslate = hapiToCommandPrescriptionTranslate;
        this.currentD2DConnection = currentD2DConnection;
        this.initPrescription();
    }

    private void initPrescription() throws IOException {
        File json = new ClassPathResource("PrescriptionBundle.json").getFile();
        FileInputStream file = new FileInputStream(json);
        String lineReadtest = readFromInputStream(file);
        IParser parser = FhirContext.forR4().newJsonParser();
        Bundle prescription = parser.parseResource(Bundle.class, lineReadtest);
        try {
            this.prescriptionFromJSON = prescription;
            this.prescriptionTranslatedFromJSON = this.currentPatient.getTranslateService().translate(prescription, Locale.ITALY, Locale.UK);
        } catch (Exception e) {
            log.error("Error calling translation service.", e);
            this.prescriptionTranslatedFromJSON = prescription;
        }
    }

    @Override
    public PrescriptionCommand prescriptionCommand() {
        List<PrescriptionInfoCommand> prescriptionInfoCommandList = new ArrayList<>();

        if (Objects.isNull(this.currentPatient.getPrescription())) {
            log.info("On plain JSON Prescription");
            if (this.currentPatient.getDisplayTranslatedVersion()) {
                var prescriptionList = this.prescriptionTranslatedFromJSON.getEntry()
                        .stream()
                        .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.MedicationRequest))
                        .map(Bundle.BundleEntryComponent::getResource)
                        .map(MedicationRequest.class::cast)
                        .map(this.hapiToCommandPrescriptionTranslate::convert)
                        .collect(Collectors.toList());
                prescriptionInfoCommandList.addAll(prescriptionList);
                prescriptionInfoCommandList.addAll(this.prescriptionsUploadedToSEHR);
                toSortMethodCommand(prescriptionInfoCommandList);
                return PrescriptionCommand.builder()
                        .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                        .prescriptionInfoCommand(prescriptionInfoCommandList)
                        .build();
            } else {
                var prescriptionList = this.prescriptionFromJSON.getEntry()
                        .stream()
                        .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.MedicationRequest))
                        .map(Bundle.BundleEntryComponent::getResource)
                        .map(MedicationRequest.class::cast)
                        .map(this.hapiToCommandPrescription::convert)
                        .collect(Collectors.toList());
                prescriptionInfoCommandList.addAll(prescriptionList);
                prescriptionInfoCommandList.addAll(this.prescriptionsUploadedToSEHR);
                toSortMethodCommand(prescriptionInfoCommandList);
                return PrescriptionCommand.builder()
                        .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                        .prescriptionInfoCommand(prescriptionInfoCommandList)
                        .build();
            }
        } else {
            if (this.currentPatient.getDisplayTranslatedVersion()) {
                var prescriptions = this.currentPatient.prescriptionListTranslated()
                        .stream()
                        .map(this.hapiToCommandPrescriptionTranslate::convert)
                        .collect(Collectors.toList());
                prescriptionInfoCommandList.addAll(prescriptions);
                prescriptionInfoCommandList.addAll(this.prescriptionsUploadedToSEHR);
                toSortMethodCommand(prescriptionInfoCommandList);
                return PrescriptionCommand.builder()
                        .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                        .prescriptionInfoCommand(prescriptionInfoCommandList)
                        .build();
            } else {
                var prescriptions = this.currentPatient.prescriptionList()
                        .stream()
                        .map(hapiToCommandPrescription::convert)
                        .collect(Collectors.toList());
                prescriptionInfoCommandList.addAll(prescriptions);
                prescriptionInfoCommandList.addAll(this.prescriptionsUploadedToSEHR);
                toSortMethodCommand(prescriptionInfoCommandList);
                return PrescriptionCommand.builder()
                        .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                        .prescriptionInfoCommand(prescriptionInfoCommandList)
                        .build();
            }

        }
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
        prescriptionInfoCommand.setTimings(prescriptionInfoCommand.getFrequency() + " times per day");
        prescriptionInfoCommand.setAuthor(healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " " + healthCareProfessionalService.getHealthCareProfessional().getLastName());

        PrescriptionEntity prescriptionEntity = this.commandToEntityPrescription.convert(prescriptionInfoCommand);
        prescriptionEntity.setAuthor(prescriptionInfoCommand.getAuthor());

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
        oldPrescription.setTimings(prescriptionInfoCommand.getFrequency() + " times per day");
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
        Bundle prescription = new Bundle();
        prescription.setEntry(new ArrayList<>());
        for (int i = 0; i < this.prescriptionRepository.findAll().size(); i++) {
            prescription.getEntry().add(new Bundle.BundleEntryComponent());
            MedicationRequest med = createPrescriptionFromEntity(this.prescriptionRepository.findAll().get(i));
            prescription.getEntry().get(i).setResource(med);
            this.prescriptionsUploadedToSEHR.add(this.hapiToCommandPrescription.convert(med));
        }
        this.sendPrescription(prescription);
    }

    @Override
    public void sendPrescription(Bundle medicationRequest) throws IOException {
        this.currentD2DConnection.getConnectedThread().sendPrescription(medicationRequest);
        log.info("Prescription sent to S-EHR");
        this.prescriptionInfoCommands.clear();
        this.prescriptionRepository.deleteAll();
    }

    private static List<PrescriptionInfoCommand> toSortMethodCommand(List<PrescriptionInfoCommand> med) {
        med.sort((o1, o2) -> {
            if (o1.getStatus().equalsIgnoreCase("Active") && (o2.getStatus().equalsIgnoreCase("On-Hold") || o2.getStatus().equalsIgnoreCase("On Hold"))) {
                return -1;
            }
            if (o1.getStatus().equalsIgnoreCase("Active") && o2.getStatus().equalsIgnoreCase("Stopped")) {
                return -1;
            }
            if ((o1.getStatus().equalsIgnoreCase("On-Hold") || o1.getStatus().equalsIgnoreCase("On Hold")) && o2.getStatus().equalsIgnoreCase("Stopped")) {
                return -1;
            }
            if (o1.getStatus().equalsIgnoreCase("Stopped") && (o2.getStatus().equalsIgnoreCase("On-Hold") || o2.getStatus().equalsIgnoreCase("On Hold"))) {
                return 1;
            }
            if (o1.getStatus().equalsIgnoreCase("Stopped") && o2.getStatus().equalsIgnoreCase("Active")) {
                return 1;
            }
            if ((o1.getStatus().equalsIgnoreCase("On-Hold") || o1.getStatus().equalsIgnoreCase("On Hold")) && o2.getStatus().equalsIgnoreCase("Active")) {
                return 1;
            }
            return 0;
        });
        return med;
    }

    private static MedicationRequest createPrescriptionFromEntity(PrescriptionEntity prescriptionEntity) {
        MedicationRequest medicationRequest = new MedicationRequest();

        medicationRequest.setMedication(new CodeableConcept().setText(prescriptionEntity.getDrugName()));

        Timing t = new Timing();
        t.getRepeat().setFrequency(prescriptionEntity.getFrequency());
        t.getRepeat().setPeriod(prescriptionEntity.getPeriod());
        t.getRepeat().setPeriodUnit(Timing.UnitsOfTime.fromCode(prescriptionEntity.getPeriodUnit()));
        List<Dosage> d = new ArrayList<>();
        d.add(new Dosage().setTiming(t));
        d.get(0).setDoseAndRate(new ArrayList<>());
        d.get(0).getDoseAndRateFirstRep().getDoseQuantity().setUnit(prescriptionEntity.getDrugDosage());

        medicationRequest.getDosageInstructionFirstRep().getTiming().getRepeat().addChild("boundsPeriod");
        Date dateStart = Date.from(prescriptionEntity.getStart().atStartOfDay(ZoneId.systemDefault()).toInstant());
        medicationRequest.getDosageInstructionFirstRep().getTiming().getRepeat().getBoundsPeriod().setStart(dateStart);

        medicationRequest.setDosageInstruction(d);
        medicationRequest.setStatus(MedicationRequest.MedicationRequestStatus.fromCode(prescriptionEntity.getStatus().toLowerCase()));

        medicationRequest.setAuthoredOn(Date.from(prescriptionEntity.getDateOfPrescription().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        medicationRequest.setId(prescriptionEntity.getId().toString());

        List<CodeableConcept> d2 = new ArrayList<>();
        d2.add(new CodeableConcept().setText(prescriptionEntity.getNotes()));
        medicationRequest.getDosageInstructionFirstRep().setAdditionalInstruction(d2);

        if (Objects.nonNull(prescriptionEntity.getEnd())) {
            Date dateEnd = Date.from(prescriptionEntity.getEnd().atStartOfDay(ZoneId.systemDefault()).toInstant());
            medicationRequest.getDosageInstructionFirstRep().getTiming().getRepeat().getBoundsPeriod().setEnd(dateEnd);
        }

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
