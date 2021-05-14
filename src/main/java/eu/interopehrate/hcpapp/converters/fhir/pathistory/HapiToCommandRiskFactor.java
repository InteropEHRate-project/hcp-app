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

        if (Objects.nonNull(source.getCode()) && source.getCode().getCodingFirstRep().hasDisplay()) {
            patHistoryInfoCommandRiskFactor.setRiskFactor(source.getCode().getCodingFirstRep().getDisplay());
        }
        if (Objects.nonNull(source.getCode()) && source.getCode().getCodingFirstRep().hasDisplayElement() &&
                source.getCode().getCodingFirstRep().getDisplayElement().hasExtension() &&
                Objects.nonNull(source.getCode().getCodingFirstRep().getDisplayElement().getExtensionFirstRep()) &&
                source.getCode().getCodingFirstRep().getDisplayElement().getExtensionFirstRep().hasExtension()) {
            patHistoryInfoCommandRiskFactor.setRiskFactorTranslated(source.getCode().getCodingFirstRep().getDisplayElement().getExtension().get(0).getExtension().get(1).getValue().toString());
        }
        if (source.hasValue() && ((BooleanType) source.getValue()).hasValue()) {
            patHistoryInfoCommandRiskFactor.setState(((BooleanType) source.getValue()).getValue());
        }

        return patHistoryInfoCommandRiskFactor;
    }
}
