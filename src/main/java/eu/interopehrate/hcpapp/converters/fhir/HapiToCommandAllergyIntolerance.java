package eu.interopehrate.hcpapp.converters.fhir;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergyIntoleranceInfoCommand;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class HapiToCommandAllergyIntolerance implements Converter<AllergyIntolerance, AllergyIntoleranceInfoCommand> {
    private final CurrentPatient currentPatient;

    public HapiToCommandAllergyIntolerance(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public AllergyIntoleranceInfoCommand convert(AllergyIntolerance allergyIntolerance) {
        AllergyIntoleranceInfoCommand command = new AllergyIntoleranceInfoCommand();

        if (Objects.nonNull(allergyIntolerance.getCode())) {
            for (Coding coding : allergyIntolerance.getCode().getCoding()) {
                command.setName(CurrentPatient.extractExtensionText(coding, this.currentPatient));
            }

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
