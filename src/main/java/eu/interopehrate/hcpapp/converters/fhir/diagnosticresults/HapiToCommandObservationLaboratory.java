package eu.interopehrate.hcpapp.converters.fhir.diagnosticresults;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommandAnalysis;
import org.hl7.fhir.exceptions.FHIRException;
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

        try {
            if (Objects.nonNull(observation.getValueQuantity()) && Objects.nonNull(observation.getValueQuantity().getValue()) ){
                command.getObservationLaboratoryInfoCommandSample().setCurrentValue(observation.getValueQuantity().getValue().doubleValue());
            }
        } catch (FHIRException e) {
//            "String found instead of Double"
        }

        try {
            if (Objects.nonNull(observation.getValueQuantity()) && Objects.nonNull(observation.getValueQuantity().getUnit()) ){
                command.getObservationLaboratoryInfoCommandSample().setUnit(observation.getValueQuantity().getUnit());
            }
        } catch (FHIRException e) {
//            "String found instead of Double"
        }


        if (Objects.nonNull(observation.getEffectiveDateTimeType())){
                command.getObservationLaboratoryInfoCommandSample().setSample(observation.getEffectiveDateTimeType().getValueAsCalendar().toZonedDateTime().toLocalDateTime());
        }

        if (Objects.nonNull(observation.getReferenceRange()) && Objects.nonNull(observation.getReferenceRangeFirstRep().getText())) {
            String range = observation.getReferenceRangeFirstRep().getText().replaceAll(",", ".");
            if (range.startsWith("<")) {
                range = range.substring(range.indexOf("<") + 2);
                if (range.contains(" ")) {
                    range = range.substring(0, range.indexOf(" "));
                }
                command.getObservationLaboratoryInfoCommandSample().setUpperLimitBound(Double.parseDouble(range));
                command.getObservationLaboratoryInfoCommandSample().setLowerLimitBound(0);
            } else if (range.contains("(")) {
                range = range.substring(0, range.indexOf("(") - 1);
                command.getObservationLaboratoryInfoCommandSample().setLowerLimitBound(Double.parseDouble(range.substring(0, range.indexOf(" "))));
                command.getObservationLaboratoryInfoCommandSample().setUpperLimitBound(Double.parseDouble(range.substring(range.indexOf("-") + 2)));
            } else {
                command.getObservationLaboratoryInfoCommandSample().setLowerLimitBound(Double.parseDouble(range.substring(0, range.indexOf(" "))));
                command.getObservationLaboratoryInfoCommandSample().setUpperLimitBound(Double.parseDouble(range.substring(range.indexOf("-") + 2)));
            }
        }

        return command;
    }
}
