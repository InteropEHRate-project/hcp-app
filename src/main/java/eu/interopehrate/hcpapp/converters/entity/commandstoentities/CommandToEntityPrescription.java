package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityPrescription implements Converter<PrescriptionInfoCommand, PrescriptionEntity> {

    @Override
    public PrescriptionEntity convert(PrescriptionInfoCommand prescriptionInfoCommand) {
        PrescriptionEntity prescriptionEntity = new PrescriptionEntity();
        prescriptionEntity.setDrugName(prescriptionInfoCommand.getDrugName());
        prescriptionEntity.setDrugDosage(prescriptionInfoCommand.getDrugDosage());
        prescriptionEntity.setFrequency(prescriptionInfoCommand.getFrequency());
        prescriptionEntity.setPeriod(prescriptionInfoCommand.getPeriod());
        prescriptionEntity.setPeriodUnit(prescriptionInfoCommand.getPeriodUnit());
        prescriptionEntity.setTimings(prescriptionInfoCommand.getTimings());
        prescriptionEntity.setStatus(prescriptionInfoCommand.getStatus());
        prescriptionEntity.setNotes(prescriptionInfoCommand.getNotes());
        prescriptionEntity.setDateOfPrescription(prescriptionInfoCommand.getDateOfPrescription());
        prescriptionEntity.setStart(prescriptionInfoCommand.getStart());
        prescriptionEntity.setEnd(prescriptionInfoCommand.getEnd());
        return prescriptionEntity;
    }
}
