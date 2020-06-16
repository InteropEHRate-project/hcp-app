package eu.interopehrate.hcpapp.converters.fhir;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseInfoCommand;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class HapiToCommandCurrentDisease implements Converter<Condition, CurrentDiseaseInfoCommand> {

    private final CurrentPatient currentPatient;

    public HapiToCommandCurrentDisease(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public CurrentDiseaseInfoCommand convert(Condition condition) {
        CurrentDiseaseInfoCommand currentDiseaseInfoCommand = new CurrentDiseaseInfoCommand();

        if (Objects.nonNull(condition.getCode())) {
            currentDiseaseInfoCommand.setCode(condition.getCode()
                    .getCoding()
                    .stream()
                    .map(Coding::getCode)
                    .collect(Collectors.joining("; ")));

            for(Coding coding : condition.getCode().getCoding()) {
                currentDiseaseInfoCommand.setName(CurrentPatient.extractExtensionText(coding, this.currentPatient));
            }
        }
        if (Objects.nonNull(condition.getOnset())) {
            Date onSet = ((DateTimeType) condition.getOnset()).getValue();
            currentDiseaseInfoCommand.setOnSet(onSet.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        if (Objects.nonNull(condition.getCategory())) {
            currentDiseaseInfoCommand.setCategoryCode(condition.getCategory()
                    .stream()
                    .flatMap(codeableConcept -> codeableConcept.getCoding().stream())
                    .map(Coding::getCode)
                    .collect(Collectors.joining("; ")));

            for(CodeableConcept codeableConcept : condition.getCategory()) {
                for(Coding coding : codeableConcept.getCoding()) {
                    currentDiseaseInfoCommand.setCategoryName(CurrentPatient.extractExtensionText(coding, this.currentPatient));
                }
            }
        }
        if (Objects.nonNull(condition.getClinicalStatus())) {
            currentDiseaseInfoCommand.setClinicalStatus(condition.getClinicalStatus()
                    .getCoding()
                    .stream()
                    .map(Coding::getCode)
                    .collect(Collectors.joining("; ")));
        }
        if (Objects.nonNull(condition.getVerificationStatus())) {
            currentDiseaseInfoCommand.setVerificationStatus(condition.getVerificationStatus()
                    .getCoding()
                    .stream()
                    .map(Coding::getCode)
                    .collect(Collectors.joining(" ")));
        }

        return currentDiseaseInfoCommand;
    }
}
