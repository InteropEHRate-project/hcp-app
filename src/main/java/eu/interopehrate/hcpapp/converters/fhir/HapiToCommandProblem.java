package eu.interopehrate.hcpapp.converters.fhir;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ProblemInfoCommand;
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
public class HapiToCommandProblem implements Converter<Condition, ProblemInfoCommand> {

    @Override
    public ProblemInfoCommand convert(Condition condition) {
        ProblemInfoCommand problemInfoCommand = new ProblemInfoCommand();

        if (Objects.nonNull(condition.getCode())) {
            problemInfoCommand.setCode(condition.getCode()
                    .getCoding()
                    .stream()
                    .map(Coding::getCode)
                    .collect(Collectors.joining("; ")));
            problemInfoCommand.setName(condition.getCode()
                    .getCoding()
                    .stream()
                    .map(Coding::getDisplay)
                    .collect(Collectors.joining("; ")));
        }
        if (Objects.nonNull(condition.getOnset())) {
            Date onSet = ((DateTimeType) condition.getOnset()).getValue();
            problemInfoCommand.setOnSet(onSet.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        if (Objects.nonNull(condition.getCategory())) {
            problemInfoCommand.setCategoryCode(condition.getCategory()
                    .stream()
                    .flatMap(codeableConcept -> codeableConcept.getCoding().stream())
                    .map(Coding::getCode)
                    .collect(Collectors.joining("; ")));
            problemInfoCommand.setCategoryName(condition.getCategory()
                    .stream()
                    .flatMap(codeableConcept -> codeableConcept.getCoding().stream())
                    .map(Coding::getDisplay)
                    .collect(Collectors.joining("; ")));
        }
        if (Objects.nonNull(condition.getClinicalStatus())) {
            problemInfoCommand.setClinicalStatus(condition.getClinicalStatus()
                    .getCoding()
                    .stream()
                    .map(Coding::getCode)
                    .collect(Collectors.joining("; ")));
        }
        if (Objects.nonNull(condition.getVerificationStatus())) {
            problemInfoCommand.setVerificationStatus(condition.getVerificationStatus()
                    .getCoding()
                    .stream()
                    .map(Coding::getCode)
                    .collect(Collectors.joining(" ")));
        }

        return problemInfoCommand;
    }
}
