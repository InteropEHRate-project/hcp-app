package eu.interopehrate.hcpapp.converters.fhir.medicationsummary;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HapiToCommandPrescription implements Converter<MedicationRequest, MedicationSummaryPrescriptionInfoCommand> {
    @Override
    public MedicationSummaryPrescriptionInfoCommand convert(MedicationRequest source) {
        MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionCommand = new MedicationSummaryPrescriptionInfoCommand();
        if(source.getMedicationCodeableConcept().hasText()) {
            medicationSummaryPrescriptionCommand.setDrugName(source.getMedicationCodeableConcept().getText());
        }
        if(source.hasStatus()) {
            medicationSummaryPrescriptionCommand.setStatus(source.getStatus().getDisplay());
        }
        return medicationSummaryPrescriptionCommand;
    }
}
