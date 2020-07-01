package eu.interopehrate.hcpapp.converters.fhir.medicationsummary;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.PrescriptionInfoCommand;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class HapiToCommandPrescriptionTranslate implements Converter<MedicationRequest, PrescriptionInfoCommand> {
    private final CurrentPatient currentPatient;

    public HapiToCommandPrescriptionTranslate(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public PrescriptionInfoCommand convert(MedicationRequest source) {
        PrescriptionInfoCommand prescriptionInfoCommand = new PrescriptionInfoCommand();
        if (source.hasMedicationCodeableConcept() && source.getMedicationCodeableConcept().hasCoding()) {
            if (this.currentPatient.getDisplayTranslatedVersion() && source.getMedicationCodeableConcept().getCoding().get(0).hasExtension()) {
                prescriptionInfoCommand.setDrugName(source.getMedicationCodeableConcept().getCoding().get(0).getExtension().get(0).getExtension().get(1).getValue().toString());
            } else {
                prescriptionInfoCommand.setDrugName(source.getMedicationCodeableConcept().getCoding().get(0).getDisplay());
            }
        }
        if (source.hasDosageInstruction() && source.getDosageInstructionFirstRep().hasAdditionalInstruction()) {
            prescriptionInfoCommand.setNotes(source.getDosageInstructionFirstRep().getAdditionalInstructionFirstRep().getText());
        }
        if(source.hasStatus() && source.getStatus().getDisplay() != null) {
            prescriptionInfoCommand.setStatus(source.getStatus().getDisplay());
        }
        if (source.hasAuthoredOn()) {
            prescriptionInfoCommand.setDateOfPrescription(source.getAuthoredOn()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        if (source.hasDosageInstruction()) {
            StringBuilder timing = new StringBuilder();
            if (source.getDosageInstructionFirstRep().hasTiming()
                    && source.getDosageInstructionFirstRep().getTiming().hasRepeat()
                    && source.getDosageInstructionFirstRep().getTiming().getRepeat().hasFrequency()
                    && source.getDosageInstructionFirstRep().getTiming().getRepeat().hasPeriod()
                    && source.getDosageInstructionFirstRep().getTiming().getRepeat().hasPeriodUnit()) {
                timing.append(source.getDosageInstructionFirstRep().getTiming().getRepeat().getFrequency());
                timing.append(" times per ");
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
            prescriptionInfoCommand.setTimings(timing.toString());
            StringBuilder drugDosage = new StringBuilder();
            if (source.getDosageInstructionFirstRep().hasDoseAndRate()
                    && source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().hasDoseQuantity()
                    && source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().hasUnit()
                    && source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().hasValue()) {
                drugDosage.append(source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().getValue() + " ");
                drugDosage.append(source.getDosageInstructionFirstRep().getDoseAndRateFirstRep().getDoseQuantity().getUnit());
            }
            prescriptionInfoCommand.setDrugDosage(drugDosage.toString());
        }

        return prescriptionInfoCommand;
    }
}
