package eu.interopehrate.hcpapp.converters.fhir.pathistory;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryInfoCommandRiskFactor;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class HapiToCommandRiskFactor implements Converter<Observation, PatHistoryInfoCommandRiskFactor> {
    private final CurrentPatient currentPatient;

    public HapiToCommandRiskFactor(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public PatHistoryInfoCommandRiskFactor convert(Observation source) {
        PatHistoryInfoCommandRiskFactor patHistoryInfoCommandRiskFactor = new PatHistoryInfoCommandRiskFactor();

        if (Objects.nonNull(source.getCode())) {
            source.getCode().getCoding().forEach(coding -> patHistoryInfoCommandRiskFactor.setRiskFactor(CurrentPatient.extractExtensionText(coding, this.currentPatient)));
        }
        if (source.hasValue() && ((BooleanType) source.getValue()).hasValue()) {
            patHistoryInfoCommandRiskFactor.setState(((BooleanType) source.getValue()).getValue());
        }

        return patHistoryInfoCommandRiskFactor;
    }
}
