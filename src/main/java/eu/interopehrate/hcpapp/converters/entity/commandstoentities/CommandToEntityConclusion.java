package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.DiagnosticConclusionEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.DiagnosticConclusionInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityConclusion implements Converter<DiagnosticConclusionInfoCommand, DiagnosticConclusionEntity> {

    @Override
    public DiagnosticConclusionEntity convert(DiagnosticConclusionInfoCommand source) {
        DiagnosticConclusionEntity diagnosticConclusionEntity = new DiagnosticConclusionEntity();
        BeanUtils.copyProperties(source, diagnosticConclusionEntity);
        return diagnosticConclusionEntity;
    }
}
