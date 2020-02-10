package eu.interopehrate.hcpapp.converters.fhir;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ProblemsInfoCommand;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;

import org.hl7.fhir.r4.model.DateTimeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class HapiToCommandProblems implements Converter<Condition, ProblemsInfoCommand> {

    @Override
    public ProblemsInfoCommand convert(Condition condition) {
        ProblemsInfoCommand problemsInfoCommand = new ProblemsInfoCommand();

        if(!CollectionUtils.isEmpty(condition.getCode().getCoding())){
            problemsInfoCommand.setNameProblem(condition
                    .getCode()
                    .getCoding()
                    .stream()
                    .map(Coding::getDisplay)
                    .collect(Collectors.joining(",")));
            problemsInfoCommand.setCode(condition
                    .getCode()
                    .getCoding()
                    .stream()
                    .map(Coding::getCode)
                    .collect(Collectors.joining(",")));
        }

        if(Objects.nonNull(condition.getId())){
            problemsInfoCommand.setIdProblem(condition.getIdBase());
        }

        if (Objects.nonNull(condition.getOnsetDateTimeType())) {
            Date onsetDateTime = ((DateTimeType)condition.getOnsetDateTimeType()).getValue();
            problemsInfoCommand.setDate(onsetDateTime.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        return problemsInfoCommand;
    }
}
