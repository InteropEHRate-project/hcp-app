package eu.interopehrate.hcpapp.converters.fhir.currentmedications;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import org.hl7.fhir.r4.model.*;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class HapiToCommandPrescription implements Converter<MedicationStatement, PrescriptionInfoCommand> {
    private final CurrentPatient currentPatient;

    public HapiToCommandPrescription(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public PrescriptionInfoCommand convert(MedicationStatement source) {
        PrescriptionInfoCommand prescriptionInfoCommand = new PrescriptionInfoCommand();
        prescriptionInfoCommand.setIdFHIR(source.getId());
        if (source.hasId()) {
            prescriptionInfoCommand.setId(Long.parseLong((((Reference) source.getMedication()).getResource().getIdElement().getIdPart())));
        }

        if (source.hasMedication()) {
            prescriptionInfoCommand.setDrugName((((Medication) ((Reference) source.getMedication()).getResource()).getCode().getCoding().get(0).getDisplayElement().toString()));
        }

        if (source.hasMedication()) {
            //  prescriptionInfoCommand.setDrugNameTranslated((((Medication) ((Reference) source.getMedication()).getResource()).getCode().getCoding().get(0).getDisplayElement().getExtension().get(0).getExtension().get(1).getValue().toString()));
        }
        if (this.currentPatient.getDisplayTranslatedVersion() && source.hasDosage() && source.getDosageFirstRep().hasRoute()
                && source.getDosageFirstRep().getRoute().hasCoding() && source.getDosageFirstRep().getRoute().getCodingFirstRep().hasDisplayElement()
                && source.getDosageFirstRep().getRoute().getCodingFirstRep().getDisplayElement().hasExtension()
                && source.getDosageFirstRep().getRoute().getCodingFirstRep().getDisplayElement().getExtensionFirstRep().hasExtension()) {
            prescriptionInfoCommand.setNotes(source.getDosageFirstRep().getRoute().getCodingFirstRep().getDisplay());
        } else if (source.hasDosage() && source.getDosageFirstRep().hasRoute() && source.getDosageFirstRep().getRoute().hasCoding()) {
            prescriptionInfoCommand.setNotes(source.getDosageFirstRep().getRoute().getCodingFirstRep().getDisplay());
        }
        if (source.hasDosage() && source.getDosageFirstRep().hasRoute() &&
                source.getDosageFirstRep().getRoute().hasCoding() && source.getDosageFirstRep().getRoute().getCodingFirstRep().getDisplayElement().hasExtension()) {
            prescriptionInfoCommand.setNotesTranslated(source.getDosageFirstRep().getRoute().getCodingFirstRep().getDisplayElement().getExtensionFirstRep().getExtension().get(1).getValue().toString());
        }
        if (source.hasStatus() && source.getStatus().getDisplay() != null) {
            prescriptionInfoCommand.setStatus(source.getStatus().toString());
        }
//        if (source.hasAuthoredOn()) {
//            prescriptionInfoCommand.setDateOfPrescription(source.getAuthoredOn()
//                    .toInstant()
//                    .atZone(ZoneId.systemDefault())
//                    .toLocalDate());
//        }
        if (source.hasDosage()) {
            if (source.getDosageFirstRep().hasTiming()
                    && source.getDosageFirstRep().getTiming().hasRepeat()
                    && source.getDosageFirstRep().getTiming().getRepeat().hasFrequency()) {
                prescriptionInfoCommand.setTimings(source.getDosageFirstRep().getTiming().getRepeat().getFrequency() + "");
            }
            if (source.getDosageFirstRep().hasTiming()
                    && source.getDosageFirstRep().getTiming().hasRepeat()
                    && source.getDosageFirstRep().getTiming().getRepeat().hasBoundsPeriod()) {
                if (source.getDosageFirstRep().getTiming().getRepeat().getBoundsPeriod().hasStart()) {
                    prescriptionInfoCommand.setStart(source.getDosageFirstRep().getTiming().getRepeat().getBoundsPeriod().getStart()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                }
                if (source.getDosageFirstRep().getTiming().getRepeat().getBoundsPeriod().hasEnd()) {
                    prescriptionInfoCommand.setEnd(source.getDosageFirstRep().getTiming().getRepeat().getBoundsPeriod().getEnd()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                }
            }
            StringBuilder drugDosage = new StringBuilder();
            if (source.getDosageFirstRep().hasDoseAndRate()
                    && source.getDosageFirstRep().getDoseAndRateFirstRep().hasDoseQuantity()
                    && source.getDosageFirstRep().getDoseAndRateFirstRep().getDoseQuantity().hasValue()) {
                drugDosage.append(source.getDosageFirstRep().getDoseAndRateFirstRep().getDoseQuantity().getValue()).append(" ");
            }
            if (source.getDosageFirstRep().hasDoseAndRate()
                    && source.getDosageFirstRep().getDoseAndRateFirstRep().hasDoseQuantity()
                    && source.getDosageFirstRep().getDoseAndRateFirstRep().getDoseQuantity().hasUnit()) {
                drugDosage.append(source.getDosageFirstRep().getDoseAndRateFirstRep().getDoseQuantity().getUnit());
            }
            prescriptionInfoCommand.setDrugDosage(drugDosage.toString());
        }

        return prescriptionInfoCommand;
    }
}
