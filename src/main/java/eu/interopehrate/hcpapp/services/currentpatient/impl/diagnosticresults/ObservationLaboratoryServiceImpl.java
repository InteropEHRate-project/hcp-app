package eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults;

import eu.interopehrate.hcpapp.converters.fhir.diagnosticresults.HapiToCommandObservationLaboratory;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommandAnalysis;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ObservationLaboratoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Slf4j
@Service
public class ObservationLaboratoryServiceImpl implements ObservationLaboratoryService {
    private CurrentPatient currentPatient;
    private HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory;

    public ObservationLaboratoryServiceImpl(CurrentPatient currentPatient, HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory) {
        this.currentPatient = currentPatient;
        this.hapiToCommandObservationLaboratory = hapiToCommandObservationLaboratory;
    }

    @Override
    public ObservationLaboratoryCommandAnalysis observationLaboratoryInfoCommandAnalysis() {
        var observationLaboratoryInfoCommandAnalyses = currentPatient.laboratoryList()
                .stream()
                .map(hapiToCommandObservationLaboratory::convert)
                .collect(Collectors.toList());
        observationLaboratoryInfoCommandAnalyses.forEach(ObservationLaboratoryInfoCommandAnalysis::setIsInLimits);
        return ObservationLaboratoryCommandAnalysis.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .observationLaboratoryInfoCommandAnalyses(observationLaboratoryInfoCommandAnalyses).build();
    }
}
