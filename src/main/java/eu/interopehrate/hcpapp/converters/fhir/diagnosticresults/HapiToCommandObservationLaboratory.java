package eu.interopehrate.hcpapp.converters.fhir.diagnosticresults;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommandAnalyte;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HapiToCommandObservationLaboratory implements Converter<Observation, ObservationLaboratoryInfoCommandAnalyte> {

    @Override
    public ObservationLaboratoryInfoCommandAnalyte convert(Observation observation) {
        ObservationLaboratoryInfoCommandAnalyte command = new ObservationLaboratoryInfoCommandAnalyte();
        return command;
    }
}
