package eu.interopehrate.hcpapp.converters.fhir;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergyIntoleranceInfoCommand;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class HapiToCommandAllergyIntolerance implements Converter<AllergyIntolerance, AllergyIntoleranceInfoCommand> {

    @Override
    public AllergyIntoleranceInfoCommand convert(AllergyIntolerance allergyIntolerance) {
        AllergyIntoleranceInfoCommand command = new AllergyIntoleranceInfoCommand();
        if (Objects.nonNull(allergyIntolerance.getCriticality())) {
            command.setCriticality(allergyIntolerance.getCriticality().getDisplay());
        }
        if (Objects.nonNull(allergyIntolerance.getType())) {
            command.setType(allergyIntolerance.getType().getDisplay());
        }
        if (!CollectionUtils.isEmpty(allergyIntolerance.getCategory())) {
            command.setCategory(allergyIntolerance
                    .getCategory()
                    .stream()
                    .map(aice -> aice.getValue().getDisplay())
                    .collect(Collectors.joining(", "))
            );
        }
        if (!CollectionUtils.isEmpty(allergyIntolerance.getIdentifier())) {
            command.setIdentifier(allergyIntolerance
                    .getIdentifier()
                    .stream()
                    .map(Identifier::getValue)
                    .collect(Collectors.joining(", "))
            );
        }
        if (Objects.nonNull(allergyIntolerance.getCode())) {
            command.setName(allergyIntolerance
                    .getCode()
                    .getCoding()
                    .stream()
                    .map(Coding::getDisplay)
                    .collect(Collectors.joining(", "))
            );
            command.setCode(allergyIntolerance
                    .getCode()
                    .getCoding()
                    .stream()
                    .map(Coding::getCode)
                    .collect(Collectors.joining(", "))
            );
        }
        return command;
    }
}
