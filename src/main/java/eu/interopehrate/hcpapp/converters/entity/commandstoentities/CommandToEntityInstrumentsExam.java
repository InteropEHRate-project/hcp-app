package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.InstrumentsExaminationEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.InstrumentsExaminationInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityInstrumentsExam implements Converter<InstrumentsExaminationInfoCommand, InstrumentsExaminationEntity> {

    @Override
    public InstrumentsExaminationEntity convert(InstrumentsExaminationInfoCommand source) {
        InstrumentsExaminationEntity instrumentsExaminationEntity = new InstrumentsExaminationEntity();
        BeanUtils.copyProperties(source, instrumentsExaminationEntity);
        return instrumentsExaminationEntity;
    }
}
