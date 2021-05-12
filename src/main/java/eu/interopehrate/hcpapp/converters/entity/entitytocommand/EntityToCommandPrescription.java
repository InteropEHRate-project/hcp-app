package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandPrescription implements Converter<PrescriptionEntity, PrescriptionInfoCommand> {

    @Override
    public PrescriptionInfoCommand convert(PrescriptionEntity prescriptionEntity) {
        PrescriptionInfoCommand prescriptionInfoCommand = new PrescriptionInfoCommand();
        BeanUtils.copyProperties(prescriptionEntity, prescriptionInfoCommand);
        return prescriptionInfoCommand;
    }
}
