package eu.interopehrate.hcpapp.services.currentpatient.impl.laboratorytests;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.SpecimenCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.SpecimenInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.laboratorytests.SpecimenService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpecimenServiceImpl implements SpecimenService {
    private CurrentPatient currentPatient;
    //Hardcoded Specimen
    private SpecimenInfoCommand specimenInfoCommand = new SpecimenInfoCommand();

    public SpecimenServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public SpecimenCommand specimenInfoCommand(String id) {
        List<SpecimenInfoCommand> specimenInfoCommands = new ArrayList<>();
        this.specimenInfoCommand.setId(id);
        specimenInfoCommands.add(specimenInfoCommand);
        return SpecimenCommand.builder().displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .specimenInfoCommandList(specimenInfoCommands)
                .build();
    }
}
