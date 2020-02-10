package eu.interopehrate.hcpapp.converters.fhir;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ProblemInfoCommand;
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
public class HapiToCommandProblem implements Converter<Condition, ProblemInfoCommand> {

    @Override
    public ProblemInfoCommand convert(Condition condition) {
        ProblemInfoCommand problemInfoCommand = new ProblemInfoCommand();

        if(!CollectionUtils.isEmpty(condition.getCode().getCoding())){
            problemInfoCommand.setName(condition
                    .getCode()
                    .getCoding()
                    .stream()
                    .map(Coding::getDisplay)
                    .collect(Collectors.joining(",")));
            problemInfoCommand.setCode(condition
                    .getCode()
                    .getCoding()
                    .stream()
                    .map(Coding::getCode)
                    .collect(Collectors.joining(",")));
        }

//        if(Objects.nonNull(condition.getId())){
//            problemsInfoCommand.setIdProblem(condition.getIdBase());
//        }

        if (Objects.nonNull(condition.getOnsetDateTimeType())) {
            Date onsetDateTime = ((DateTimeType)condition.getOnsetDateTimeType()).getValue();
            problemInfoCommand.setOnSet(onsetDateTime.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }

        return problemInfoCommand;
    }
}
