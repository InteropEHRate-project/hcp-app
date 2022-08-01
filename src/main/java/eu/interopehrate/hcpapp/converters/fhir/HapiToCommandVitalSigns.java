package eu.interopehrate.hcpapp.converters.fhir;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsInfoCommand;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Quantity;
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
            vitalSignsInfoCommand.setAnalysisName(observation.getCode().getCodingFirstRep().getDisplay());
        }

        if (Objects.nonNull(observation.getCode()) && observation.getCode().getCodingFirstRep().hasDisplayElement() &&
                Objects.nonNull(observation.getCode().getCodingFirstRep().getDisplayElement().getExtensionFirstRep()) &&
                observation.getCode().getCodingFirstRep().getDisplayElement().getExtensionFirstRep().hasExtension()) {
            vitalSignsInfoCommand.setAnalysisNameTranslated(observation.getCode().getCodingFirstRep().getDisplayElement().getExtensionFirstRep().getExtension().get(1).getValue().toString());
        }

        try {
            if (Objects.nonNull(observation.getEffectiveDateTimeType())) {
                vitalSignsInfoCommand.getVitalSignsInfoCommandSample().setLocalDateOfVitalSign(observation.getEffectiveDateTimeType().getValueAsCalendar().toZonedDateTime().toLocalDateTime());
            }
        } catch (NullPointerException e) {
            System.out.println("Null value date for Vital Signs.");
        }

        try {
            if (Objects.nonNull(observation.getValueQuantity()) && Objects.nonNull(observation.getValueQuantity().getValue())) {
                vitalSignsInfoCommand.getVitalSignsInfoCommandSample().setCurrentValue(((Quantity) observation.getValue()).getValue().doubleValue());
            } else {
                vitalSignsInfoCommand.getVitalSignsInfoCommandSample().setCurrentValue(Double.parseDouble(observation.getValueStringType().getValue()));
            }
        } catch (FHIRException e) {
            System.out.println("Incorrect type for quantity.");
        }

        try {
            if (Objects.nonNull(observation.getValueQuantity()) && Objects.nonNull(observation.getValueQuantity().getValue())) {
                vitalSignsInfoCommand.getVitalSignsInfoCommandSample().setUnitOfMeasurement(observation.getValueQuantity().getUnit());
            } else {
                vitalSignsInfoCommand.getVitalSignsInfoCommandSample().setUnitOfMeasurement(observation.getValueStringType().getValue());
            }
        } catch (FHIRException e) {
            System.out.println("Incorrect type for quantity.");
        }
        return vitalSignsInfoCommand;
    }
}
