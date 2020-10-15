package eu.interopehrate.hcpapp.services.currentpatient.impl.currentmedications;

import eu.interopehrate.hcpapp.converters.entity.EntityToCommandPrescription;
import eu.interopehrate.hcpapp.converters.entity.commandstoentities.CommandToEntityPrescription;
import eu.interopehrate.hcpapp.converters.fhir.currentmedications.HapiToCommandPrescription;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandPrescription hapiToCommandPrescription;
    private CommandToEntityPrescription commandToEntityPrescription = new CommandToEntityPrescription();
    private EntityToCommandPrescription entityToCommandPrescription = new EntityToCommandPrescription();
    private final PrescriptionRepository prescriptionRepository;
    @Autowired
    private HealthCareProfessionalService healthCareProfessionalService;
    private CurrentD2DConnection currentD2DConnection;
    private boolean isFiltered = false;
    private boolean isEmpty = false;

    public PrescriptionServiceImpl(CurrentPatient currentPatient, HapiToCommandPrescription hapiToCommandPrescription, PrescriptionRepository prescriptionRepository, CurrentD2DConnection currentD2DConnection) {
        this.currentPatient = currentPatient;
        this.hapiToCommandPrescription = hapiToCommandPrescription;
        this.prescriptionRepository = prescriptionRepository;
        this.currentD2DConnection = currentD2DConnection;
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
    public void insertPrescription(PrescriptionInfoCommand prescriptionInfoCommand) {
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
        PrescriptionInfoCommand oldPrescription = prescriptionInfoCommandById(prescriptionInfoCommand.getId());
        oldPrescription.setDrugName(prescriptionInfoCommand.getDrugName());
        oldPrescription.setDateOfPrescription(prescriptionInfoCommand.getDateOfPrescription());
        oldPrescription.setDrugDosage(prescriptionInfoCommand.getDrugDosage());
        oldPrescription.setNotes(prescriptionInfoCommand.getNotes());
        oldPrescription.setStatus(prescriptionInfoCommand.getStatus());
        oldPrescription.setFrequency(prescriptionInfoCommand.getFrequency());
        oldPrescription.setPeriod(prescriptionInfoCommand.getPeriod());
        oldPrescription.setPeriodUnit(prescriptionInfoCommand.getPeriodUnit());
        oldPrescription.setTimings(prescriptionInfoCommand.getFrequency().toString());
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
        prescriptionEntity.setTimings(prescriptionInfoCommand.getFrequency().toString());
        prescriptionEntity.setStart(prescriptionInfoCommand.getStart());
        prescriptionEntity.setEnd(prescriptionInfoCommand.getEnd());
        this.prescriptionRepository.save(prescriptionEntity);
    }

    @Override
    public void callSendPrescription() throws IOException {
        if (Objects.nonNull(this.currentD2DConnection.getConnectedThread())) {
            Bundle prescription = new Bundle();
            prescription.setEntry(new ArrayList<>());
            for (int i = 0; i < this.prescriptionRepository.findAll().size(); i++) {
                prescription.getEntry().add(new Bundle.BundleEntryComponent());
                MedicationRequest med = createPrescriptionFromEntity(this.prescriptionRepository.findAll().get(i));
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
    public void sendPrescription(Bundle medicationRequest) throws IOException {
        this.currentD2DConnection.getConnectedThread().sendPrescription(medicationRequest);
        log.info("Prescription sent to S-EHR");
        this.prescriptionRepository.deleteAll();
    }

    @Override
    public Page<PrescriptionEntity> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<PrescriptionEntity> listOfPrescription = this.prescriptionRepository.findAll();
        toSortMethodEntity(listOfPrescription);
        return createPageFromListofEntity(listOfPrescription, pageable, pageNo, pageSize);
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

    private static MedicationRequest createPrescriptionFromEntity(PrescriptionEntity prescriptionEntity) {
        MedicationRequest medicationRequest = new MedicationRequest();

        medicationRequest.setMedication(new CodeableConcept());
        medicationRequest.getMedication().addChild("coding");
        medicationRequest.getMedicationCodeableConcept().setCoding(new ArrayList<>());
        medicationRequest.getMedicationCodeableConcept().getCoding().add(new Coding());
        medicationRequest.getMedicationCodeableConcept().getCoding().get(0).setDisplay(prescriptionEntity.getDrugName());

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
}
