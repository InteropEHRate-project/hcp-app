package eu.interopehrate.hcpapp.converters.fhir.pathistory;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryInfoCommandDiagnosis;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class HapiToCommandDiagnosis implements Converter<Condition, PatHistoryInfoCommandDiagnosis> {
    private final CurrentPatient currentPatient;

    public HapiToCommandDiagnosis(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public PatHistoryInfoCommandDiagnosis convert(Condition source) {
        PatHistoryInfoCommandDiagnosis patHistoryInfoCommandDiagnosis = new PatHistoryInfoCommandDiagnosis();

        if (Objects.nonNull(source.getCode())) {
            source.getCode().getCoding().forEach(coding -> patHistoryInfoCommandDiagnosis.setDiagnosis(CurrentPatient.extractExtensionText(coding, this.currentPatient)));
        }
        if (source.hasOnset() && Objects.nonNull(((DateTimeType)source.getOnset()).getYear())) {
            patHistoryInfoCommandDiagnosis.setYearOfDiagnosis(((DateTimeType)source.getOnset()).getYear());
        }
        if (source.hasNote() && source.getNoteFirstRep().hasText()) {
            patHistoryInfoCommandDiagnosis.setComments(source.getNoteFirstRep().getText());
        }

        return patHistoryInfoCommandDiagnosis;
    }
}
