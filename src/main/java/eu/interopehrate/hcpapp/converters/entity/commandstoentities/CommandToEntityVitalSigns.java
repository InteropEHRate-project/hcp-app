package eu.interopehrate.hcpapp.converters.entity.commandstoentities;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.VitalSignsEntity;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.VitalSignsTypesEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.VitalSignsTypesRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsInfoCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CommandToEntityVitalSigns implements Converter<VitalSignsInfoCommand, VitalSignsEntity> {
    private final VitalSignsTypesRepository vitalSignsTypesRepository;

    public CommandToEntityVitalSigns(VitalSignsTypesRepository vitalSignsTypesRepository) {
        this.vitalSignsTypesRepository = vitalSignsTypesRepository;
    }

    @Override
    public VitalSignsEntity convert(VitalSignsInfoCommand source) {
        VitalSignsEntity vitalSignsEntity = new VitalSignsEntity();
        vitalSignsEntity.setPatientId(source.getPatientId());
        vitalSignsEntity.setAuthor(source.getAuthor());

        for (VitalSignsTypesEntity vt : this.vitalSignsTypesRepository.findAll()) {
            if (vt.getName().equalsIgnoreCase(source.getAnalysisName())) {
                vitalSignsEntity.setAnalysisType(vt);
                break;
            }
        }
        if (Objects.isNull(vitalSignsEntity.getAnalysisType())) {
            throw new IllegalArgumentException("Analysis type cannot be found in the HCP local Data Base");
        }
        vitalSignsEntity.setLocalDateOfVitalSign(source.getVitalSignsInfoCommandSample().getLocalDateOfVitalSign());
        vitalSignsEntity.setCurrentValue(source.getVitalSignsInfoCommandSample().getCurrentValue());
        vitalSignsEntity.setUnitOfMeasurement(source.getVitalSignsInfoCommandSample().getUnitOfMeasurement());
        return vitalSignsEntity;
    }
}
