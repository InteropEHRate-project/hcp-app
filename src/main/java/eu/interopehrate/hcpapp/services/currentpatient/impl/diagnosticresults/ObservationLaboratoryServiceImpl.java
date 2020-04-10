package eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ObservationLaboratoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ObservationLaboratoryServiceImpl implements ObservationLaboratoryService {
    private CurrentPatient currentPatient;
    private List<ObservationLaboratoryInfoCommand> observationLaboratoryInfoCommands = new ArrayList<>();
    //Hardcoded records in Observation Laboratory result tabel
    private ObservationLaboratoryInfoCommand observationLaboratoryInfoCommand1 = new ObservationLaboratoryInfoCommand();
    private ObservationLaboratoryInfoCommand observationLaboratoryInfoCommand2 = new ObservationLaboratoryInfoCommand();
    private ObservationLaboratoryInfoCommand observationLaboratoryInfoCommand3 = new ObservationLaboratoryInfoCommand();
    private ObservationLaboratoryInfoCommand observationLaboratoryInfoCommand4 = new ObservationLaboratoryInfoCommand();

    public ObservationLaboratoryServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public ObservationLaboratoryCommand observationLaboratoryInfoCommand() {
        List<ObservationLaboratoryInfoCommand> observationLaboratoryInfoCommands = new ArrayList<>();
        observationLaboratoryInfoCommands.add(observationLaboratoryInfoCommand1);
        observationLaboratoryInfoCommands.add(observationLaboratoryInfoCommand2);
        observationLaboratoryInfoCommands.add(observationLaboratoryInfoCommand3);
        observationLaboratoryInfoCommands.add(observationLaboratoryInfoCommand4);
        return ObservationLaboratoryCommand.builder().displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .observationLaboratoryInfoCommands(observationLaboratoryInfoCommands).build();
    }

    @Override
    public void insertObservationLaboratory(ObservationLaboratoryInfoCommand observationLaboratoryInfoCommand) {
        this.observationLaboratoryInfoCommands.add(observationLaboratoryInfoCommand);
    }
}
