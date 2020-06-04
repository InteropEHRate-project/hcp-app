package eu.interopehrate.hcpapp.converters.fhir.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class HapiToCommandPrescription implements Converter<MedicationRequest, MedicationSummaryPrescriptionInfoCommand> {
    @Override
    public MedicationSummaryPrescriptionInfoCommand convert(MedicationRequest source) {
        MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand = new MedicationSummaryPrescriptionInfoCommand();
        if (Objects.nonNull(source)) {
            if(source.getMedicationCodeableConcept().hasText()) {
                medicationSummaryPrescriptionInfoCommand.setDrugName(source.getMedicationCodeableConcept().getText());
            }
            if(source.hasStatus()) {
                medicationSummaryPrescriptionInfoCommand.setStatus(source.getStatus().getDisplay());
            }
        }
        return medicationSummaryPrescriptionInfoCommand;
    }
}
