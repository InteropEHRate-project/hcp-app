package eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults;

import eu.interopehrate.hcpapp.converters.fhir.diagnosticresults.HapiToCommandObservationLaboratory;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ObservationLaboratoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ObservationLaboratoryServiceImpl implements ObservationLaboratoryService {
    private CurrentPatient currentPatient;
    private HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory;
    private List<ObservationLaboratoryInfoCommand> observationLaboratoryInfoCommands = new ArrayList<>();

    public ObservationLaboratoryServiceImpl(CurrentPatient currentPatient, HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory) {
        this.currentPatient = currentPatient;
        this.hapiToCommandObservationLaboratory = hapiToCommandObservationLaboratory;
    }

    @Override
    public ObservationLaboratoryCommand observationLaboratoryInfoCommand() {
        var observationLaboratories = currentPatient.observationList()
                .stream()
                .map(hapiToCommandObservationLaboratory::convert)
                .collect(Collectors.toList());
        observationLaboratories.addAll(observationLaboratoryInfoCommands);
        return ObservationLaboratoryCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .observationLaboratoryInfoCommands(observationLaboratories)
                .build();
    }

    @Override
    public void insertObservationLaboratory(ObservationLaboratoryInfoCommand observationLaboratoryInfoCommand) {
        this.observationLaboratoryInfoCommands.add(observationLaboratoryInfoCommand);
    }
}
