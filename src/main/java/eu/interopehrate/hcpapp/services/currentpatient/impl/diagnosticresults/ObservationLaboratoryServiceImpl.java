package eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults;

import eu.interopehrate.hcpapp.converters.fhir.diagnosticresults.HapiToCommandObservationLaboratory;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommandAnalysis;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ObservationLaboratoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ObservationLaboratoryServiceImpl implements ObservationLaboratoryService {
    private CurrentPatient currentPatient;
    private HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory;
    private List<ObservationLaboratoryInfoCommandAnalysis> observationLaboratoryInfoCommandAnalysis = new ArrayList<>();

    public ObservationLaboratoryServiceImpl(CurrentPatient currentPatient, HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory) {
        this.currentPatient = currentPatient;
        this.hapiToCommandObservationLaboratory = hapiToCommandObservationLaboratory;
    }

    @Override
    public ObservationLaboratoryCommandAnalysis observationLaboratoryInfoCommandAnalysis() {
        List<ObservationLaboratoryInfoCommandAnalysis> observationLaboratoryInfoCommandAnalyses = currentPatient.getObservation()
                .stream()
                .map(hapiToCommandObservationLaboratory::convert)
                .collect(Collectors.toList());
        observationLaboratoryInfoCommandAnalyses.addAll(observationLaboratoryInfoCommandAnalysis);

        return ObservationLaboratoryCommandAnalysis.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .observationLaboratoryInfoCommandAnalyses(observationLaboratoryInfoCommandAnalyses).build();
    }

}
