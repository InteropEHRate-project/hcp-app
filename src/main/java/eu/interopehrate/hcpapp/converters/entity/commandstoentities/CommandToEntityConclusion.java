package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.DiagnosticConclusionEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.ConclusionInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CommandToEntityConclusion implements Converter<ConclusionInfoCommand, DiagnosticConclusionEntity> {

    @Override
    public DiagnosticConclusionEntity convert(ConclusionInfoCommand source) {
        DiagnosticConclusionEntity diagnosticConclusionEntity = new DiagnosticConclusionEntity();
        BeanUtils.copyProperties(source, diagnosticConclusionEntity);
        return diagnosticConclusionEntity;
    }
}
