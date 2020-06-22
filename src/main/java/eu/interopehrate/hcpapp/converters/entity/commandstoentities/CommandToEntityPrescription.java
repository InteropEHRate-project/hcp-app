package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityPrescription implements Converter<MedicationSummaryPrescriptionInfoCommand, PrescriptionEntity> {

    @Override
    public PrescriptionEntity convert(MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand) {
        PrescriptionEntity prescriptionEntity = new PrescriptionEntity();
        prescriptionEntity.setDrugName(medicationSummaryPrescriptionInfoCommand.getDrugName());
        prescriptionEntity.setDrugDosage(medicationSummaryPrescriptionInfoCommand.getDrugDosage());
        prescriptionEntity.setTimings(medicationSummaryPrescriptionInfoCommand.getTimings().replace("<br/>", ", "));
        prescriptionEntity.setStatus(medicationSummaryPrescriptionInfoCommand.getStatus());
        prescriptionEntity.setNotes(medicationSummaryPrescriptionInfoCommand.getNotes());
        prescriptionEntity.setDateOfPrescription(medicationSummaryPrescriptionInfoCommand.getDateOfPrescription());
        return prescriptionEntity;
    }
}
