package eu.interopehrate.hcpapp.converters.fhir;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyInfoCommand;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Identifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class HapiToCommandAllergy implements Converter<AllergyIntolerance, AllergyInfoCommand> {
    private final CurrentPatient currentPatient;

    public HapiToCommandAllergy(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public AllergyInfoCommand convert(AllergyIntolerance allergyIntolerance) {
        AllergyInfoCommand command = new AllergyInfoCommand();
        command.setIdFHIR(allergyIntolerance.getId());

        if (Objects.nonNull(allergyIntolerance.getCode())) {
            // using the method that doesn't take into account the value of "displayTranaslatedVersion" boolean
            allergyIntolerance.getCode().getCoding().forEach(coding -> command.setName(CurrentPatient.testExtractExtensionText(coding)));

            if (Objects.nonNull(allergyIntolerance.getCategory())) {
                command.setCategory(allergyIntolerance.getCategory()
                        .stream()
                        .map(aice -> aice.getValue().getDisplay())
                        .collect(Collectors.joining("; "))
                );
            }

            if (Objects.nonNull(allergyIntolerance.getType())) {
                command.setType(allergyIntolerance.getType().getDisplay());
            }

            if (Objects.nonNull(allergyIntolerance.getIdentifier())) {
                command.setIdentifier(allergyIntolerance.getIdentifier()
                        .stream()
                        .map(Identifier::getValue)
                        .collect(Collectors.joining("; "))
                );
            }
        }
        return command;
    }
}
