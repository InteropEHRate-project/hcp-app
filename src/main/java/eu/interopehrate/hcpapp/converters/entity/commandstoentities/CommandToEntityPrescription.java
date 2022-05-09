package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.PrescriptionEntity;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.PrescriptionTypesEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PrescriptionTypesRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityPrescription implements Converter<PrescriptionInfoCommand, PrescriptionEntity> {
    private final PrescriptionTypesRepository prescriptionTypesRepository;

    public CommandToEntityPrescription(PrescriptionTypesRepository prescriptionTypesRepository) {
        this.prescriptionTypesRepository = prescriptionTypesRepository;
    }

    @Override
    public PrescriptionEntity convert(PrescriptionInfoCommand prescriptionInfoCommand) {
        PrescriptionEntity prescriptionEntity = new PrescriptionEntity();
        BeanUtils.copyProperties(prescriptionInfoCommand, prescriptionEntity);
        for (PrescriptionTypesEntity vt : this.prescriptionTypesRepository.findAll()) {
            if (vt.getName().equalsIgnoreCase(prescriptionInfoCommand.getDrugName())) {
                prescriptionEntity.setPrescriptionTypesEntity(vt);
                break;
            }
        }
        return prescriptionEntity;
    }
}
