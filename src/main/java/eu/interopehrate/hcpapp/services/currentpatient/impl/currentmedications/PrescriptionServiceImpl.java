package eu.interopehrate.hcpapp.services.currentpatient.impl.currentmedications;

import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityPrescription;
import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandPrescription;
import eu.interopehrate.hcpapp.converters.fhir.currentmedications.HapiToCommandPrescription;
import eu.interopehrate.hcpapp.currentsession.CloudConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.PrescriptionEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PrescriptionTypesRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.hcpapp.services.administration.HealthCareProfessionalService;
import eu.interopehrate.hcpapp.services.currentpatient.currentmedications.PrescriptionService;
import eu.interopehrate.ihs.terminalclient.fhir.TerminalFhirContext;
import eu.interopehrate.ihs.terminalclient.services.TranslateService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandPrescription hapiToCommandPrescription;
    private final CommandToEntityPrescription commandToEntityPrescription = new CommandToEntityPrescription();
    private final EntityToCommandPrescription entityToCommandPrescription = new EntityToCommandPrescription();
    private final PrescriptionRepository prescriptionRepository;
    @Autowired
    private HealthCareProfessionalService healthCareProfessionalService;
    private final CurrentD2DConnection currentD2DConnection;
    private boolean isFiltered = false;
    private boolean isEmpty = false;
    private final AuditInformationService auditInformationService;
    private final PrescriptionTypesRepository prescriptionTypesRepository;
    private final CloudConnection cloudConnection;
    private final TranslateService translateService;
    @Autowired
    private TerminalFhirContext terminalFhirContext;

    public PrescriptionServiceImpl(CurrentPatient currentPatient, HapiToCommandPrescription hapiToCommandPrescription, PrescriptionRepository prescriptionRepository,
                                   CurrentD2DConnection currentD2DConnection, AuditInformationService auditInformationService,
                                   PrescriptionTypesRepository prescriptionTypesRepository, CloudConnection cloudConnection, TranslateService translateService) {
        this.currentPatient = currentPatient;
        this.hapiToCommandPrescription = hapiToCommandPrescription;
        this.prescriptionRepository = prescriptionRepository;
        this.currentD2DConnection = currentD2DConnection;
        this.auditInformationService = auditInformationService;
        this.prescriptionTypesRepository = prescriptionTypesRepository;
        this.cloudConnection = cloudConnection;
        this.translateService = translateService;
    }

    @Override
    public PrescriptionTypesRepository getPrescriptionTypesRepository() {
        return prescriptionTypesRepository;
    }

    @Override
    public CurrentPatient getCurrentPatient() {
        return currentPatient;
    }

    @Override
    public PrescriptionRepository getPrescriptionRepository() {
        return prescriptionRepository;
    }

    @Override
    public void setFiltered(boolean filtered) {
        isFiltered = filtered;
    }

    @Override
    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    @Override
    public boolean isFiltered() {
        return this.isFiltered;
    }

    @Override
    public boolean isEmpty() {
        return this.isEmpty;
    }

    @Override
    public CurrentD2DConnection getCurrentD2DConnection() {
        return currentD2DConnection;
    }

    //The method can filter and return only records that contains the keyword in the drugName if it is the case.
    @Override
    public PrescriptionCommand prescriptionCommand(int pageNo, int pageSize, String keyword) {
        var prescriptions = this.currentPatient.prescriptionList()
                .stream()
                .map(this.hapiToCommandPrescription::convert)
                .collect(Collectors.toList());

        if (keyword == null || keyword.equals("empty") || keyword.trim().equals("")) {
            this.isFiltered = false;
            this.isEmpty = false;
            toSortMethodCommand(prescriptions);
            //Pagination generation
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<PrescriptionInfoCommand> page = createPageFromListOfCommand(prescriptions, pageable, pageNo, pageSize);
            return PrescriptionCommand.builder()
                    .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                    .pageInfoCommand(page)
                    .build();
        }

        if (Objects.nonNull(keyword) && !keyword.trim().equals("")) {
            //The filtration is happening...
            List<PrescriptionInfoCommand> prescriptionInfoCommandList = new ArrayList<>();
            prescriptions.forEach((pr) -> {
                if (pr.getDrugName().toLowerCase().contains(keyword.toLowerCase())) {
                    prescriptionInfoCommandList.add(pr);
                }
            });
            if (prescriptionInfoCommandList.isEmpty()) {
                this.isEmpty = true;
                this.isFiltered = false;
            } else {
                this.isFiltered = true;
                this.isEmpty = false;
            }
            toSortMethodCommand(prescriptionInfoCommandList);
            //Pagination generation
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<PrescriptionInfoCommand> page = createPageFromListOfCommand(prescriptionInfoCommandList, pageable, pageNo, pageSize);
            return PrescriptionCommand.builder()
                    .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                    .pageInfoCommand(page)
                    .build();
        }
        this.isFiltered = false;
        this.isEmpty = false;
        toSortMethodCommand(prescriptions);

        //Pagination generation
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<PrescriptionInfoCommand> page = createPageFromListOfCommand(prescriptions, pageable, pageNo, pageSize);
        return PrescriptionCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .pageInfoCommand(page)
                .build();
    }

    @Override
    public PrescriptionInfoCommand prescriptionInfoCommandById(Long id) {
        if (this.prescriptionRepository.findById(id).isPresent()) {
            return this.entityToCommandPrescription.convert(this.prescriptionRepository.findById(id).get());
        } else {
            throw new NoSuchElementException("id not found");
        }
    }

    @Override
    public PrescriptionInfoCommand retrievePrescriptionFromSEHRById(String id) {
        var prescription = this.currentPatient.getPrescriptionTranslated().getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.MedicationStatement) && resource.getId().equals(id)).findFirst();
        return prescription.map(resource -> this.hapiToCommandPrescription.convert((MedicationStatement) resource)).orElse(null);
    }

    @Override
    public void updatePrescriptionFromSEHR(PrescriptionInfoCommand prescriptionInfoCommand) {
        // update translated bundle
        var optional = this.currentPatient.getPrescriptionTranslated().getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.MedicationStatement) && resource.getId().equals(prescriptionInfoCommand.getIdFHIR())).findFirst();
        updatePrescriptionDetails(optional, prescriptionInfoCommand);

        // update original bundle
        optional = this.currentPatient.getPrescription().getEntry()
                .stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .filter(resource -> resource.getResourceType().equals(ResourceType.MedicationStatement) && resource.getId().equals(prescriptionInfoCommand.getIdFHIR())).findFirst();
        updatePrescriptionDetails(optional, prescriptionInfoCommand);
    }

    @Override
    public void insertPrescription(PrescriptionInfoCommand prescriptionInfoCommand) {
        prescriptionInfoCommand.setPatientId(this.currentPatient.getPatient().getId());
        prescriptionInfoCommand.setTimings(prescriptionInfoCommand.getFrequency().toString());
        prescriptionInfoCommand.setAuthor(healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " " + healthCareProfessionalService.getHealthCareProfessional().getLastName());

        PrescriptionEntity prescriptionEntity = this.commandToEntityPrescription.convert(prescriptionInfoCommand);
        prescriptionEntity.setAuthor(prescriptionInfoCommand.getAuthor());

        prescriptionEntity.setPeriodUnit(toShortUnit(prescriptionEntity.getPeriodUnit()));
        this.prescriptionRepository.save(prescriptionEntity);
        //Adding the ID from the database to the InfoCommand
        prescriptionInfoCommand.setId(prescriptionEntity.getId());
    }

    @Override
    public void deletePrescription(Long drugId) {
        this.prescriptionRepository.deleteById(drugId);
    }

    @Override
    public void updatePrescription(PrescriptionInfoCommand prescriptionInfoCommand) {
        prescriptionInfoCommand.setPatientId(this.currentPatient.getPatient().getId());
        prescriptionInfoCommand.setAuthor(this.healthCareProfessionalService.getHealthCareProfessional().getFirstName() + " " +
                this.healthCareProfessionalService.getHealthCareProfessional().getLastName());
        PrescriptionInfoCommand oldPrescription = prescriptionInfoCommandById(prescriptionInfoCommand.getId());
        BeanUtils.copyProperties(prescriptionInfoCommand, oldPrescription);
        oldPrescription.setTimings(prescriptionInfoCommand.getFrequency().toString());

        PrescriptionEntity prescriptionEntity = this.prescriptionRepository.getOne(prescriptionInfoCommand.getId());
        BeanUtils.copyProperties(prescriptionInfoCommand, prescriptionEntity);
        prescriptionEntity.setPeriodUnit(toShortUnit(prescriptionInfoCommand.getPeriodUnit()));
        prescriptionEntity.setTimings(prescriptionInfoCommand.getFrequency().toString());
        this.prescriptionRepository.save(prescriptionEntity);
    }

    @Override
    public void callSendPrescription() {
        if (Objects.nonNull(this.currentD2DConnection.getTd2D())) {
            Bundle prescription = new Bundle();
            prescription.setEntry(new ArrayList<>());
            for (int i = 0; i < this.prescriptionRepository.findAll().size(); i++) {
                prescription.getEntry().add(new Bundle.BundleEntryComponent());
                MedicationStatement med = createPrescriptionFromEntity(this.prescriptionRepository.findAll().get(i));
                prescription.getEntry().get(i).setResource(med);
                this.currentPatient.getPrescription().getEntry().add(new Bundle.BundleEntryComponent().setResource(med));
                this.currentPatient.getPrescriptionTranslated().getEntry().add(new Bundle.BundleEntryComponent().setResource(med));
            }
            this.sendPrescription(prescription);
        } else {
            log.error("The connection with S-EHR is not established.");
        }
    }

    @Override
    @SneakyThrows
    public void sendPrescription(Bundle medicationRequest) {
        this.currentD2DConnection.getTd2D().sendHealthData(medicationRequest);
//      this.currentD2DConnection.getConnectedThread().sendPrescription(medicationRequest);
        log.info("Prescription sent to S-EHR");
        auditInformationService.auditEvent(AuditEventType.SEND_TO_SEHR, "Auditing send Prescription to S-EHR");
        this.prescriptionRepository.deleteAll();
    }

    @Override
    public Page<PrescriptionEntity> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<PrescriptionEntity> listOfPrescription = this.prescriptionRepository.findAll();
        toSortMethodEntity(listOfPrescription);
        return createPageFromListofEntity(listOfPrescription, pageable, pageNo, pageSize);
    }

    @Override
    public void refreshData() {
        this.cloudConnection.downloadPrescription();
    }

    private static void toSortMethodCommand(List<PrescriptionInfoCommand> med) {
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
    }

    private static void toSortMethodEntity(List<PrescriptionEntity> med) {
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
    }

    private static MedicationStatement createPrescriptionFromEntity(PrescriptionEntity prescriptionEntity) {
        MedicationStatement medicationStatement = new MedicationStatement();

        medicationStatement.setMedication(new Reference());
        ((Reference) medicationStatement.getMedication()).setResource(new Medication().setCode(new CodeableConcept().setCoding(new ArrayList<>())
                .addCoding(new Coding()
                        .setSystem("https://loinc.org")
                        .setCode("52471-0")
                        .setDisplay(prescriptionEntity.getDrugName()))));

        Timing t = new Timing();
        t.getRepeat().setFrequency(prescriptionEntity.getFrequency());
        t.getRepeat().setPeriod(prescriptionEntity.getPeriod());
        t.getRepeat().setPeriodUnit(Timing.UnitsOfTime.fromCode(prescriptionEntity.getPeriodUnit()));

        List<Dosage> d = new ArrayList<>();
        d.add(new Dosage().setTiming(t));
        d.get(0).setDoseAndRate(new ArrayList<>());
        d.get(0).getDoseAndRateFirstRep().getDoseQuantity().setUnit(prescriptionEntity.getDrugDosage());
        medicationStatement.setDosage(d);
        medicationStatement.getDosageFirstRep().getTiming().getRepeat().addChild("boundsPeriod");
        Date dateStart = Date.from(prescriptionEntity.getStart().atStartOfDay(ZoneId.systemDefault()).toInstant());
        medicationStatement.getDosageFirstRep().getTiming().getRepeat().getBoundsPeriod().setStart(dateStart);

        medicationStatement.setStatus(MedicationStatement.MedicationStatementStatus.fromCode(prescriptionEntity.getStatus().toLowerCase()));
        medicationStatement.addExtension().setValue(new Signature().setWhen(Date.from(prescriptionEntity.getDateOfPrescription().atStartOfDay(ZoneId.systemDefault()).toInstant())).
                setWho(medicationStatement.getInformationSource().setReference(prescriptionEntity.getAuthor())).setTargetFormat("json").setSigFormat("application/jose"));

        medicationStatement.getInformationSource().setReference(prescriptionEntity.getAuthor());
        (medicationStatement.getMedication()).setId(prescriptionEntity.getId().toString());

        List<CodeableConcept> d2 = new ArrayList<>();
        d2.add(new CodeableConcept().setText(prescriptionEntity.getNotes()));
        medicationStatement.getDosageFirstRep().setAdditionalInstruction(d2);

        if (Objects.nonNull(prescriptionEntity.getEnd())) {
            Date dateEnd = Date.from(prescriptionEntity.getEnd().atStartOfDay(ZoneId.systemDefault()).toInstant());
            medicationStatement.getDosageFirstRep().getTiming().getRepeat().getBoundsPeriod().setEnd(dateEnd);
        }
        return medicationStatement;
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

    private static Page<PrescriptionInfoCommand> createPageFromListOfCommand(List<PrescriptionInfoCommand> list, Pageable pageable, int pageNo, int pageSize) {
        try {
            int index = (pageNo - 1) * pageSize;
            return new PageImpl<>(list.subList(index, index + pageSize), pageable, list.size());
        } catch (IndexOutOfBoundsException ignored) {
            int index = (pageNo - 1) * pageSize;
            return new PageImpl<>(list.subList(index, list.size()), pageable, list.size());
        }
    }

    private static Page<PrescriptionEntity> createPageFromListofEntity(List<PrescriptionEntity> list, Pageable pageable, int pageNo, int pageSize) {
        try {
            int index = (pageNo - 1) * pageSize;
            return new PageImpl<>(list.subList(index, index + pageSize), pageable, list.size());
        } catch (IndexOutOfBoundsException ignored) {
            int index = (pageNo - 1) * pageSize;
            return new PageImpl<>(list.subList(index, list.size()), pageable, list.size());
        }
    }

    private static void updatePrescriptionDetails(Optional<Resource> optional, PrescriptionInfoCommand prescriptionInfoCommand) {
        if (optional.isPresent()) {
            ((MedicationStatement) optional.get()).setStatus(MedicationStatement.MedicationStatementStatus.valueOf(prescriptionInfoCommand.getStatus().toUpperCase()));

            // deletes Notes translation if the prescription's notes are different
            StringType displayElement = ((MedicationStatement) optional.get()).getDosageFirstRep().getRoute().getCodingFirstRep().getDisplayElement();
            if (displayElement.hasExtension() && !displayElement.getValue().equalsIgnoreCase(prescriptionInfoCommand.getNotes())) {
                displayElement.getExtension().clear();
            }

            ((MedicationStatement) optional.get()).getDosageFirstRep().getRoute().getCodingFirstRep().setDisplay(prescriptionInfoCommand.getNotes());

            ((MedicationStatement) optional.get()).getDosageFirstRep().getTiming().getRepeat().setFrequency(Integer.parseInt(prescriptionInfoCommand.getTimings()));
            // ((MedicationStatement) optional.get()).setAuthoredOn(Date.from(prescriptionInfoCommand.getDateOfPrescription().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            ((MedicationStatement) optional.get()).getDosageFirstRep().getTiming().getRepeat().getBoundsPeriod()
                    .setStart(Date.from(prescriptionInfoCommand.getStart().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            if (Objects.nonNull(prescriptionInfoCommand.getEnd())) {
                ((MedicationStatement) optional.get()).getDosageFirstRep().getTiming().getRepeat().getBoundsPeriod()
                        .setEnd(Date.from(prescriptionInfoCommand.getEnd().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
        } else {
            log.error("Cannot be updated. Resource not found.");
        }
    }
}
