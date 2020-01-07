package eu.interopehrate.hcpapp.converters.fhir;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.DiagnosticResultInfoCommand;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class HapiToCommandDiagnosticResults implements Converter<Observation, DiagnosticResultInfoCommand> {

    @Override
    public DiagnosticResultInfoCommand convert(Observation observation) {
        DiagnosticResultInfoCommand diagnosticResultInfoCommand = new DiagnosticResultInfoCommand();

        if(!CollectionUtils.isEmpty(observation.getCode().getCoding())){
            diagnosticResultInfoCommand.setNameDiagnostic(observation
                    .getCode()
                    .getCoding()
                    .stream()
                    .map(Coding::getDisplay)
                    .collect(Collectors.joining(", ")));
        }

        if(Objects.nonNull(observation.getValueQuantity().getValue())){
            diagnosticResultInfoCommand.setValue(observation.getValueQuantity().getValue().doubleValue());
        }

        if(Objects.nonNull(observation.getValueQuantity().getUnit())){
            diagnosticResultInfoCommand.setUnitOfMeasurement(observation.getValueQuantity().getUnit());
        }

        return diagnosticResultInfoCommand;
    }
}
