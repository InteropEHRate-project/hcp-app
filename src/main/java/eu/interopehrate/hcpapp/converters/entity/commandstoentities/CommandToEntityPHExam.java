package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.PHExamEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam.PHExamInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityPHExam implements Converter<PHExamInfoCommand, PHExamEntity> {

    @Override
    public PHExamEntity convert(PHExamInfoCommand source) {
        PHExamEntity phExamEntity = new PHExamEntity();
        BeanUtils.copyProperties(source, phExamEntity);
        return phExamEntity;
    }
}
