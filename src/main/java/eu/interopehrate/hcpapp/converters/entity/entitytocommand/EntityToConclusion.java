package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.DiagnosticConclusionEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.ConclusionInfoCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToConclusion implements Converter<DiagnosticConclusionEntity, ConclusionInfoCommand> {

    @Override
    public ConclusionInfoCommand convert(DiagnosticConclusionEntity diagnosticConclusionEntity) {
        ConclusionInfoCommand conclusionInfoCommand = new ConclusionInfoCommand();
        BeanUtils.copyProperties(diagnosticConclusionEntity, conclusionInfoCommand);
        return conclusionInfoCommand;
    }
}
