package eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.converters.fhir.diagnosticresults.HapiToCommandObservationLaboratory;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommandAnalysis;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ObservationLaboratoryService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ObservationLaboratoryServiceImpl implements ObservationLaboratoryService {
    private CurrentPatient currentPatient;
    private HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory;
    private List<ObservationLaboratoryInfoCommandAnalysis> observationLaboratoryInfoCommandAnalysis = new ArrayList<>();
    private Bundle labResultsBundle;

    @SneakyThrows
    public ObservationLaboratoryServiceImpl(CurrentPatient currentPatient, HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory) {
        this.currentPatient = currentPatient;
        this.hapiToCommandObservationLaboratory = hapiToCommandObservationLaboratory;

        File json = new ClassPathResource("LaboratoryResultsWithRanges.json").getFile();
        FileInputStream file = new FileInputStream(json);
        String lineReadtest = readFromInputStream(file);
        IParser parser = FhirContext.forR4().newJsonParser();
        this.labResultsBundle = parser.parseResource(Bundle.class, lineReadtest);
    }

    @Override
    public ObservationLaboratoryCommandAnalysis observationLaboratoryInfoCommandAnalysis() {
        if (Objects.isNull(this.currentPatient.getLaboratoryResults())) {
            var observations = this.labResultsBundle.getEntry()
                    .stream()
                    .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.Observation))
                    .map(Bundle.BundleEntryComponent::getResource)
                    .map(Observation.class::cast)
                    .collect(Collectors.toList());
            var observationLaboratoryInfoCommandAnalyses = observations
                    .stream()
                    .map(hapiToCommandObservationLaboratory::convert)
                    .collect(Collectors.toList());

            for (ObservationLaboratoryInfoCommandAnalysis e : observationLaboratoryInfoCommandAnalyses) {
                e.setIsInLimits();
            }
            log.info("LAB RESULTS from PLAIN JSON with LIMITS !!!");

            observationLaboratoryInfoCommandAnalyses.addAll(observationLaboratoryInfoCommandAnalysis);
            return ObservationLaboratoryCommandAnalysis.builder()
                    .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                    .observationLaboratoryInfoCommandAnalyses(observationLaboratoryInfoCommandAnalyses).build();
        }

        List<ObservationLaboratoryInfoCommandAnalysis> observationLaboratoryInfoCommandAnalyses = currentPatient.laboratoryList()
                .stream()
                .map(hapiToCommandObservationLaboratory::convert)
                .collect(Collectors.toList());

        for (ObservationLaboratoryInfoCommandAnalysis e : observationLaboratoryInfoCommandAnalyses) {
            e.setIsInLimits();
        }

        observationLaboratoryInfoCommandAnalyses.addAll(observationLaboratoryInfoCommandAnalysis);

        return ObservationLaboratoryCommandAnalysis.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .observationLaboratoryInfoCommandAnalyses(observationLaboratoryInfoCommandAnalyses).build();
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
