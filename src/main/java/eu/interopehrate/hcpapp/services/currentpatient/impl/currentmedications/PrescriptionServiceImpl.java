package eu.interopehrate.hcpapp.services.currentpatient.impl.currentmedications;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private boolean isFiltered = false;
    private boolean isEmpty = false;

    public PrescriptionServiceImpl(CurrentPatient currentPatient, HapiToCommandPrescription hapiToCommandPrescription, CurrentD2DConnection currentD2DConnection) {
        this.currentPatient = currentPatient;
        this.hapiToCommandPrescription = hapiToCommandPrescription;
        this.currentD2DConnection = currentD2DConnection;
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
    public PrescriptionCommand prescriptionCommand(String keyword) {
        var prescriptions = this.currentPatient.prescriptionList()
                .stream()
                .map(this.hapiToCommandPrescription::convert)
                .collect(Collectors.toList());
        toSortMethodCommand(prescriptions);

        if (Objects.nonNull(keyword) && !keyword.equals("")) {
            //The filtration is happening...
            List<PrescriptionInfoCommand> prescriptionInfoCommandList = new ArrayList<>();
            for (PrescriptionInfoCommand pr : prescriptions) {
                if (pr.getDrugName().toLowerCase().contains(keyword.toLowerCase())) {
                    prescriptionInfoCommandList.add(pr);
                }
            }
            if (prescriptionInfoCommandList.isEmpty()) {
                this.isEmpty = true;
                this.isFiltered = false;
            } else {
                this.isFiltered = true;
                this.isEmpty = false;
            }
            return PrescriptionCommand.builder()
                    .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                    .prescriptionInfoCommand(prescriptionInfoCommandList)
                    .build();
        }
        this.isFiltered = false;
        this.isEmpty = false;
        return PrescriptionCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .prescriptionInfoCommand(prescriptions)
                .build();
    }

    //The method can filter and return only records that contains the keyword in the drugName if it is the case.
    @Override
    public PrescriptionCommand prescriptionCommandUpload(String keyword) {
        if (Objects.nonNull(keyword) && !keyword.equals("")) {
            List<PrescriptionInfoCommand> prescriptionInfoCommandList = new ArrayList<>();
            for (PrescriptionInfoCommand pr : this.prescriptionInfoCommands) {
                if (pr.getDrugName().toLowerCase().contains(keyword.toLowerCase())) {
                    prescriptionInfoCommandList.add(pr);
                }
            }
            if (prescriptionInfoCommandList.isEmpty()) {
                this.isEmpty = true;
                this.isFiltered = false;
            } else {
                this.isFiltered = true;
                this.isEmpty = false;
            }
            return PrescriptionCommand.builder()
                    .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                    .prescriptionInfoCommand(prescriptionInfoCommandList)
                    .build();
        }
        this.isFiltered = false;
        this.isEmpty = false;
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
        this.prescriptionInfoCommands.clear();
        this.prescriptionRepository.deleteAll();
    }

    @Override
    public Page<PrescriptionEntity> findPaginated(int pageNo, int pageSize, String sortField, String sortDir) {
        Sort sort = Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.prescriptionRepository.findAll(pageable);
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
}
