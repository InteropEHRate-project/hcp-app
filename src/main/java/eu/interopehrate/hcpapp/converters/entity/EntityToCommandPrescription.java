package eu.interopehrate.hcpapp.converters.entity;

import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandPrescription implements Converter<PrescriptionEntity, PrescriptionInfoCommand> {

    @Override
    public PrescriptionInfoCommand convert(PrescriptionEntity prescriptionEntity) {
        PrescriptionInfoCommand prescriptionInfoCommand = new PrescriptionInfoCommand();
        prescriptionInfoCommand.setId(prescriptionEntity.getId());
        prescriptionInfoCommand.setDrugName(prescriptionEntity.getDrugName());
        prescriptionInfoCommand.setDrugDosage(prescriptionEntity.getDrugDosage());
        prescriptionInfoCommand.setFrequency(prescriptionEntity.getFrequency());
        prescriptionInfoCommand.setPeriod(prescriptionEntity.getPeriod());
        prescriptionInfoCommand.setPeriodUnit(prescriptionEntity.getPeriodUnit());
        prescriptionInfoCommand.setTimings(prescriptionEntity.getTimings());
        prescriptionInfoCommand.setStatus(prescriptionEntity.getStatus());
        prescriptionInfoCommand.setNotes(prescriptionEntity.getNotes());
        prescriptionInfoCommand.setDateOfPrescription(prescriptionEntity.getDateOfPrescription());
        prescriptionInfoCommand.setStart(prescriptionEntity.getStart());
        prescriptionInfoCommand.setEnd(prescriptionEntity.getEnd());
        return prescriptionInfoCommand;
    }
}
