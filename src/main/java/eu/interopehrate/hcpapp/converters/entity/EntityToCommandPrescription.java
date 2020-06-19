package eu.interopehrate.hcpapp.converters.entity;

import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryPrescriptionInfoCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandPrescription implements Converter<PrescriptionEntity, MedicationSummaryPrescriptionInfoCommand> {

    @Override
    public MedicationSummaryPrescriptionInfoCommand convert(PrescriptionEntity prescriptionEntity) {
        MedicationSummaryPrescriptionInfoCommand medicationSummaryPrescriptionInfoCommand = new MedicationSummaryPrescriptionInfoCommand();
        medicationSummaryPrescriptionInfoCommand.setId(prescriptionEntity.getId());
        medicationSummaryPrescriptionInfoCommand.setDrugName(prescriptionEntity.getDrugName());
        medicationSummaryPrescriptionInfoCommand.setDrugDosage(prescriptionEntity.getDrugDosage());
        medicationSummaryPrescriptionInfoCommand.setTimings(prescriptionEntity.getTimings());
        medicationSummaryPrescriptionInfoCommand.setStatus(prescriptionEntity.getStatus());
        medicationSummaryPrescriptionInfoCommand.setNotes(prescriptionEntity.getNotes());
        medicationSummaryPrescriptionInfoCommand.setDateOfPrescription(prescriptionEntity.getDateOfPrescription());
        return medicationSummaryPrescriptionInfoCommand;
    }
}
