package eu.interopehrate.hcpapp.converters.fhir.diagnosticresults;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommandAnalysis;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class HapiToCommandObservationLaboratory implements Converter<Observation, ObservationLaboratoryInfoCommandAnalysis> {
    private final CurrentPatient currentPatient;

    public HapiToCommandObservationLaboratory(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public ObservationLaboratoryInfoCommandAnalysis convert(Observation observation) {
        ObservationLaboratoryInfoCommandAnalysis command = new ObservationLaboratoryInfoCommandAnalysis();

        if (Objects.nonNull(observation.getCode())) {
            for (Coding coding : observation.getCode().getCoding()) {
                command.setAnalysis(CurrentPatient.extractExtensionText(coding, this.currentPatient));
            }
        }

        if (Objects.nonNull(observation.getValueQuantity())){
                command.getObservationLaboratoryInfoCommandSample().setValue(observation.getValueQuantity().getValue().toString());
                command.getObservationLaboratoryInfoCommandSample().setUnit(observation.getValueQuantity().getUnit());
        }

        if (Objects.nonNull(observation.getEffectiveDateTimeType())){
                command.getObservationLaboratoryInfoCommandSample().setSample(observation.getEffectiveDateTimeType().getValueAsCalendar().toZonedDateTime().toLocalDateTime());
        }

        return command;
    }
}
