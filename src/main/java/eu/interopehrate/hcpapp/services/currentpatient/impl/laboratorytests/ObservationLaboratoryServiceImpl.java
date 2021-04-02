package eu.interopehrate.hcpapp.services.currentpatient.impl.laboratorytests;

import eu.interopehrate.hcpapp.converters.fhir.laboratorytests.HapiToCommandObservationLaboratory;
import eu.interopehrate.hcpapp.currentsession.CloudConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryInfoCommandAnalysis;
import eu.interopehrate.hcpapp.services.currentpatient.laboratorytests.ObservationLaboratoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ObservationLaboratoryServiceImpl implements ObservationLaboratoryService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory;
    private boolean isFiltered = false;
    private boolean isEmpty = false;
    private final CloudConnection cloudConnection;

    public ObservationLaboratoryServiceImpl(CurrentPatient currentPatient, HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory, CloudConnection cloudConnection) {
        this.currentPatient = currentPatient;
        this.hapiToCommandObservationLaboratory = hapiToCommandObservationLaboratory;
        this.cloudConnection = cloudConnection;
    }

    @Override
    public boolean isFiltered() {
        return isFiltered;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public ObservationLaboratoryCommandAnalysis observationLaboratoryInfoCommandAnalysis(String keyword) {
        var observationLaboratoryInfoCommandAnalyses = currentPatient.laboratoryList()
                .stream()
                .map(hapiToCommandObservationLaboratory::convert)
                .collect(Collectors.toList());

        if (keyword == null || keyword.equals("empty") || keyword.trim().equals("")) {
            observationLaboratoryInfoCommandAnalyses.forEach(ObservationLaboratoryInfoCommandAnalysis::setIsInLimits);
            this.isFiltered = false;
            this.isEmpty = false;
            return ObservationLaboratoryCommandAnalysis.builder()
                    .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                    .observationLaboratoryInfoCommandAnalyses(observationLaboratoryInfoCommandAnalyses)
                    .build();
        }

        if (Objects.nonNull(keyword) && !keyword.trim().equals("")) {
            //The filtration is happening...
            List<ObservationLaboratoryInfoCommandAnalysis> laboratoryInfoCommandList = new ArrayList<>();
            observationLaboratoryInfoCommandAnalyses.forEach((lab) -> {
                if (lab.getAnalysis().toLowerCase().contains(keyword.toLowerCase())) {
                    laboratoryInfoCommandList.add(lab);
                }
            });
            if (laboratoryInfoCommandList.isEmpty()) {
                this.isEmpty = true;
                this.isFiltered = false;
            } else {
                this.isFiltered = true;
                this.isEmpty = false;
            }
            laboratoryInfoCommandList.forEach(ObservationLaboratoryInfoCommandAnalysis::setIsInLimits);
            return ObservationLaboratoryCommandAnalysis.builder()
                    .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                    .observationLaboratoryInfoCommandAnalyses(laboratoryInfoCommandList)
                    .build();
        }
        observationLaboratoryInfoCommandAnalyses.forEach(ObservationLaboratoryInfoCommandAnalysis::setIsInLimits);
        return ObservationLaboratoryCommandAnalysis.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .observationLaboratoryInfoCommandAnalyses(observationLaboratoryInfoCommandAnalyses)
                .build();
    }

    @Override
    public void refreshData() {
        this.cloudConnection.downloadLabResults();
    }
}
