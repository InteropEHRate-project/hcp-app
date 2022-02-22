package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.DiagnosticConclusionEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.DiagnosticConclusionInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToConclusion implements Converter<DiagnosticConclusionEntity, DiagnosticConclusionInfoCommand> {

    @Override
    public DiagnosticConclusionInfoCommand convert(DiagnosticConclusionEntity diagnosticConclusionEntity) {
        DiagnosticConclusionInfoCommand diagnosticConclusionInfoCommand = new DiagnosticConclusionInfoCommand();
        BeanUtils.copyProperties(diagnosticConclusionEntity, diagnosticConclusionInfoCommand);
        return diagnosticConclusionInfoCommand;
    }
}
