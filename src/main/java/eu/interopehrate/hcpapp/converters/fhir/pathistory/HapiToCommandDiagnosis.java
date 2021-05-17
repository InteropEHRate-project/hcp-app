package eu.interopehrate.hcpapp.converters.fhir.pathistory;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryInfoCommandDiagnosis;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class HapiToCommandDiagnosis implements Converter<Condition, PatHistoryInfoCommandDiagnosis> {

    @Override
    public PatHistoryInfoCommandDiagnosis convert(Condition source) {
        PatHistoryInfoCommandDiagnosis patHistoryInfoCommandDiagnosis = new PatHistoryInfoCommandDiagnosis();

        if (Objects.nonNull(source.getId())) {
            patHistoryInfoCommandDiagnosis.setId(source.getId());
        }
        if (Objects.nonNull(source.getCode()) && source.getCode().getCodingFirstRep().hasDisplay()) {
            patHistoryInfoCommandDiagnosis.setDiagnosis(source.getCode().getCodingFirstRep().getDisplay());
        }
        if (Objects.nonNull(source.getCode()) && source.getCode().getCodingFirstRep().hasDisplayElement() &&
                source.getCode().getCodingFirstRep().getDisplayElement().hasExtension() &&
                Objects.nonNull(source.getCode().getCodingFirstRep().getDisplayElement().getExtensionFirstRep()) &&
                source.getCode().getCodingFirstRep().getDisplayElement().getExtensionFirstRep().hasExtension()) {
            patHistoryInfoCommandDiagnosis.setDiagnosisTranslated(source.getCode().getCodingFirstRep().getDisplayElement().getExtension().get(0).getExtension().get(1).getValue().toString());
        }
        if (source.hasOnset() && Objects.nonNull(((DateTimeType) source.getOnset()).getYear())) {
            patHistoryInfoCommandDiagnosis.setYearOfDiagnosis(((DateTimeType) source.getOnset()).getYear());
        }
        if (source.hasNote() && source.getNoteFirstRep().hasText()) {
            patHistoryInfoCommandDiagnosis.setComments(source.getNoteFirstRep().getText());
        }

        return patHistoryInfoCommandDiagnosis;
    }
}
