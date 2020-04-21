package eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.SpecimenCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.SpecimenInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.SpecimenService;
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
