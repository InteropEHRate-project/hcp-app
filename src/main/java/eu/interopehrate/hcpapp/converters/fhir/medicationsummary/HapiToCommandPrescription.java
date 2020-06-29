package eu.interopehrate.hcpapp.converters.fhir.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Objects;

@Component
public class HapiToCommandPrescription implements Converter<MedicationRequest, MedicationSummaryPrescriptionInfoCommand> {
    @Override
    public MedicationSummaryPrescriptionInfoCommand convert(MedicationRequest source) {
        MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand = new MedicationSummaryPrescriptionInfoCommand();
        if (Objects.nonNull(source)) {
            if(source.hasMedicationCodeableConcept() && source.getMedicationCodeableConcept().hasText()) {
                medicationSummaryPrescriptionInfoCommand.setDrugName(source.getMedicationCodeableConcept().getText());
            }
            if(source.hasStatus() && source.getStatus().getDisplay() != null) {
                medicationSummaryPrescriptionInfoCommand.setStatus(source.getStatus().getDisplay());
            }
            if (source.hasAuthoredOn()) {
                medicationSummaryPrescriptionInfoCommand.setDateOfPrescription(source.getAuthoredOn()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate());
            }
            if (source.hasDosageInstruction()) {
                if (source.getDosageInstructionFirstRep().hasAdditionalInstruction() && source.getDosageInstructionFirstRep().getAdditionalInstructionFirstRep().hasText()) {
                    medicationSummaryPrescriptionInfoCommand.setNotes(source.getDosageInstructionFirstRep()
                            .getAdditionalInstructionFirstRep()
                            .getText());
                }
                StringBuilder timing = new StringBuilder();
                if (source.getDosageInstructionFirstRep().hasTiming()
                        && source.getDosageInstructionFirstRep().getTiming().hasRepeat()
                        && source.getDosageInstructionFirstRep().getTiming().getRepeat().hasFrequency()
                        && source.getDosageInstructionFirstRep().getTiming().getRepeat().hasPeriod()
                        && source.getDosageInstructionFirstRep().getTiming().getRepeat().hasPeriodUnit()) {
                    timing.append(source.getDosageInstructionFirstRep().getTiming().getRepeat().getFrequency());
                    timing.append(" times, for ");
                    timing.append(source.getDosageInstructionFirstRep().getTiming().getRepeat().getPeriod());
                    timing.append(" ");
                    switch (source.getDosageInstructionFirstRep().getTiming().getRepeat().getPeriodUnit()) {
                        case MIN:
                            timing.append("minute");
                            break;
                        case H:
                            timing.append("hour");
                            break;
                        case D:
                            timing.append("day");
                            break;
                        case WK:
                            timing.append("week");
                            break;
                        case MO:
                            timing.append("month");
                            break;
                        case A:
                            timing.append("year");
                            break;
                        default:
                            timing.append(source.getDosageInstructionFirstRep().getTiming().getRepeat().getPeriodUnit());
                            break;
                    }
                }
                medicationSummaryPrescriptionInfoCommand.setTimings(timing.toString());
                StringBuilder drugDosage = new StringBuilder();
                if (source.getDosageInstructionFirstRep().hasDoseAndRate()
                        && source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().hasDoseQuantity()
                        && source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().hasUnit()
                        && source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().hasValue()) {
                    drugDosage.append(source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().getValue() + " ");
                    drugDosage.append(source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().getUnit());
                }
                medicationSummaryPrescriptionInfoCommand.setDrugDosage(drugDosage.toString());
            }
        }
        return medicationSummaryPrescriptionInfoCommand;
    }
}
