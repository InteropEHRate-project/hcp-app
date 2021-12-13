package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.PHExamEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.PHExamInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandPHExam implements Converter<PHExamEntity, PHExamInfoCommand> {

    public PHExamInfoCommand convert(PHExamEntity phExamEntity) {
        PHExamInfoCommand phExamInfoCommand = new PHExamInfoCommand();
        BeanUtils.copyProperties(phExamEntity, phExamInfoCommand);
        return phExamInfoCommand;
    }
}
