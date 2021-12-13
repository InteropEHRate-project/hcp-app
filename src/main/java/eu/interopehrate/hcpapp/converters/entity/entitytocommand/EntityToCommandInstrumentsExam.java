package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.InstrumentsExaminationEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.InstrumentsExaminationInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandInstrumentsExam implements Converter<InstrumentsExaminationEntity, InstrumentsExaminationInfoCommand> {

    public InstrumentsExaminationInfoCommand convert(InstrumentsExaminationEntity instrumentsExaminationEntity) {
        InstrumentsExaminationInfoCommand instrumentsExaminationInfoCommand = new InstrumentsExaminationInfoCommand();
        BeanUtils.copyProperties(instrumentsExaminationEntity, instrumentsExaminationInfoCommand);
        return instrumentsExaminationInfoCommand;
    }
}
