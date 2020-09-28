package eu.interopehrate.hcpapp.converters.fhir.currentmedications;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class HapiToCommandPrescription implements Converter<MedicationRequest, PrescriptionInfoCommand> {
    private final CurrentPatient currentPatient;

    public HapiToCommandPrescription(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public PrescriptionInfoCommand convert(MedicationRequest source) {
        PrescriptionInfoCommand prescriptionInfoCommand = new PrescriptionInfoCommand();
        if (source.hasMedicationCodeableConcept() && source.getMedicationCodeableConcept().hasCoding()) {
            source.getMedicationCodeableConcept().getCoding().forEach(coding -> prescriptionInfoCommand.setDrugName(CurrentPatient.extractExtensionText(coding, this.currentPatient)));
        }
        if (source.hasDosageInstruction() && source.getDosageInstructionFirstRep().hasAdditionalInstruction()) {
            prescriptionInfoCommand.setNotes(source.getDosageInstructionFirstRep().getAdditionalInstructionFirstRep().getText());
        }
        if (source.hasStatus() && source.getStatus().getDisplay() != null) {
            prescriptionInfoCommand.setStatus(source.getStatus().getDisplay());
        }
        if (source.hasAuthoredOn()) {
            prescriptionInfoCommand.setDateOfPrescription(source.getAuthoredOn()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        if (source.hasDosageInstruction()) {
            if (source.getDosageInstructionFirstRep().hasTiming()
                    && source.getDosageInstructionFirstRep().getTiming().hasRepeat()
                    && source.getDosageInstructionFirstRep().getTiming().getRepeat().hasFrequency()) {
                prescriptionInfoCommand.setTimings(source.getDosageInstructionFirstRep().getTiming().getRepeat().getFrequency() + "");
            }
            if (source.getDosageInstructionFirstRep().hasTiming()
                    && source.getDosageInstructionFirstRep().getTiming().hasRepeat()
                    && source.getDosageInstructionFirstRep().getTiming().getRepeat().hasBoundsPeriod()) {
                if (source.getDosageInstructionFirstRep().getTiming().getRepeat().getBoundsPeriod().hasStart()) {
                    prescriptionInfoCommand.setStart(source.getDosageInstructionFirstRep().getTiming().getRepeat().getBoundsPeriod().getStart()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                }
                if (source.getDosageInstructionFirstRep().getTiming().getRepeat().getBoundsPeriod().hasEnd()) {
                    prescriptionInfoCommand.setEnd(source.getDosageInstructionFirstRep().getTiming().getRepeat().getBoundsPeriod().getEnd()
                            .toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate());
                }
            }
            StringBuilder drugDosage = new StringBuilder();
            if (source.getDosageInstructionFirstRep().hasDoseAndRate()
                    && source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().hasDoseQuantity()
                    && source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().hasValue()) {
                drugDosage.append(source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().getValue() + " ");
            }
            if (source.getDosageInstructionFirstRep().hasDoseAndRate()
                    && source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().hasDoseQuantity()
                    && source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().hasUnit()) {
                drugDosage.append(source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().getUnit());
            }
            prescriptionInfoCommand.setDrugDosage(drugDosage.toString());
        }

        return prescriptionInfoCommand;
    }
}
