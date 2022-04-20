package eu.interopehrate.hcpapp.converters.fhir;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsInfoCommand;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class HapiToCommandVitalSigns implements Converter<Observation, VitalSignsInfoCommand> {

    private final CurrentPatient currentPatient;

    public HapiToCommandVitalSigns(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public VitalSignsInfoCommand convert(Observation observation) {
        VitalSignsInfoCommand vitalSignsInfoCommand = new VitalSignsInfoCommand();

        if (Objects.nonNull(observation.getCode())) {
            observation.getCode().getCoding().forEach(coding -> vitalSignsInfoCommand.setAnalysisName(CurrentPatient.extractExtensionText(coding, this.currentPatient)));
        }

//        if (Objects.nonNull(observation.getCode()) && observation.getCode().getCodingFirstRep().hasDisplayElement() &&
//                Objects.nonNull(observation.getCode().getCodingFirstRep().getDisplayElement().getExtensionFirstRep()) &&
//                observation.getCode().getCodingFirstRep().getDisplayElement().getExtensionFirstRep().hasExtension()) {
//            vitalSignsInfoCommand.setAnalysisNameTranslated(observation.getCode().getCoding().get(1).getDisplayElement().toString());
//        }

        if (Objects.nonNull(observation.getEffectiveDateTimeType())) {
            vitalSignsInfoCommand.getVitalSignsInfoCommandSample().setLocalDateOfVitalSign(observation.getEffectiveDateTimeType().getValueAsCalendar().toZonedDateTime().toLocalDateTime());
        }

        if (Objects.nonNull(observation.getValueQuantity()) && Objects.nonNull(observation.getValueQuantity().getValue())) {
            vitalSignsInfoCommand.getVitalSignsInfoCommandSample().setCurrentValue(observation.getValueQuantity().getValue().doubleValue());
        }

        if (Objects.nonNull(observation.getValueQuantity()) && Objects.nonNull(observation.getValueQuantity().getValue())) {
            vitalSignsInfoCommand.getVitalSignsInfoCommandSample().setUnitOfMeasurement(observation.getValueQuantity().getUnit());
        }
        return vitalSignsInfoCommand;
    }
}
