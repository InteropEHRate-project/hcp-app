package eu.interopehrate.hcpapp.converters.fhir.diagnosticresults;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommandAnalysis;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HapiToCommandObservationLaboratory implements Converter<Observation, ObservationLaboratoryInfoCommandAnalysis> {

    @Override
    public ObservationLaboratoryInfoCommandAnalysis convert(Observation observation) {
        ObservationLaboratoryInfoCommandAnalysis command = new ObservationLaboratoryInfoCommandAnalysis();
        return command;
    }
}
