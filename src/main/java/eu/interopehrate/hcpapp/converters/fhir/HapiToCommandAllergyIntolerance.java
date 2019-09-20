package eu.interopehrate.hcpapp.converters.fhir;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergyIntoleranceInfoCommand;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class HapiToCommandAllergyIntolerance implements Converter<AllergyIntolerance, AllergyIntoleranceInfoCommand> {

    @Override
    public AllergyIntoleranceInfoCommand convert(AllergyIntolerance allergyIntolerance) {
        AllergyIntoleranceInfoCommand command = new AllergyIntoleranceInfoCommand();
        if (Objects.nonNull(allergyIntolerance.getCriticality())) {
            command.setCriticality(allergyIntolerance.getCriticality().getDisplay());
        }
        return command;
    }
}
