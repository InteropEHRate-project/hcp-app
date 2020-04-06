package eu.interopehrate.hcpapp.converters.fhir.diagnosticresults;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommand;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class HapiToCommandObservationLaboratory implements Converter<Observation, ObservationLaboratoryInfoCommand> {

    @Override
    public ObservationLaboratoryInfoCommand convert(Observation observation) {
        ObservationLaboratoryInfoCommand command = new ObservationLaboratoryInfoCommand();
        if (Objects.nonNull(observation.getCode())) {
            command.setCode(observation.getCode()
                    .getCoding()
                    .stream()
                    .map(Coding::getCode)
                    .collect(Collectors.joining("; "))
            );
        }
        return command;
    }
}
