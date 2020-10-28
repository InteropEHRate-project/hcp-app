package eu.interopehrate.hcpapp.services.currentpatient.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.converters.fhir.pathistory.HapiToCommandDiagnosis;
import eu.interopehrate.hcpapp.converters.fhir.pathistory.HapiToCommandRiskFactor;
import eu.interopehrate.hcpapp.currentsession.BundleProcessor;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryCommand;
import eu.interopehrate.hcpapp.services.currentpatient.PatHistoryService;
import lombok.SneakyThrows;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatHistoryServiceImpl implements PatHistoryService {
    private CurrentPatient currentPatient;
    private final List<String> listOfPatHis = new ArrayList<>();
    private final List<String> listOfSocHis = new ArrayList<>();
    private final List<String> listOfFamHis = new ArrayList<>();

    private final BundleProcessor bundleProcessor;
    private final BundleProcessor bundleProcessorTranslated;
    private final HapiToCommandRiskFactor hapiToCommandRiskFactor;
    private final HapiToCommandDiagnosis hapiToCommandDiagnosis;

    @SneakyThrows
    public PatHistoryServiceImpl(CurrentPatient currentPatient, HapiToCommandRiskFactor hapiToCommandRiskFactor, HapiToCommandDiagnosis hapiToCommandDiagnosis) {
        this.currentPatient = currentPatient;
        this.hapiToCommandRiskFactor = hapiToCommandRiskFactor;
        this.hapiToCommandDiagnosis = hapiToCommandDiagnosis;

        File json = new ClassPathResource("PathologyCompositionExampleIPS.json").getFile();
        FileInputStream file = new FileInputStream(json);
        String lineReadtest = readFromInputStream(file);
        IParser parser = FhirContext.forR4().newJsonParser();
        Bundle patHisBundle = parser.parseResource(Bundle.class, lineReadtest);
        this.currentPatient.initPatHisConsultation(patHisBundle);
        this.bundleProcessor = new BundleProcessor(this.currentPatient.getPatHisBundle());
        this.bundleProcessorTranslated = new BundleProcessor(this.currentPatient.getPatHisBundleTranslated());
    }

    @Override
    public PatHistoryCommand patHistorySection() {
        if (this.currentPatient.getDisplayTranslatedVersion()) {
            var riskFactors = this.bundleProcessorTranslated.patHisConsultationObservationsList();
            var diagnoses = this.bundleProcessorTranslated.patHisConsultationConditionsList();

            var riskFactorInfoCommands = riskFactors
                    .stream()
                    .map(hapiToCommandRiskFactor::convert)
                    .collect(Collectors.toList());
            var diagnosisInfoCommands = diagnoses
                    .stream()
                    .map(hapiToCommandDiagnosis::convert)
                    .collect(Collectors.toList());

            return PatHistoryCommand.builder()
                    .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                    .patHistoryInfoCommandRiskFactors(riskFactorInfoCommands)
                    .patHistoryInfoCommandDiagnoses(diagnosisInfoCommands)
                    .listOfPatHis(this.listOfPatHis)
                    .listOfSocHis(this.listOfSocHis)
                    .listOfFamHis(this.listOfFamHis)
                    .build();
        }
        var riskFactors = this.bundleProcessor.patHisConsultationObservationsList();
        var diagnoses = this.bundleProcessor.patHisConsultationConditionsList();

        var riskFactorInfoCommands = riskFactors
                .stream()
                .map(hapiToCommandRiskFactor::convert)
                .collect(Collectors.toList());
        var diagnosisInfoCommands = diagnoses
                .stream()
                .map(hapiToCommandDiagnosis::convert)
                .collect(Collectors.toList());

        return PatHistoryCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .patHistoryInfoCommandRiskFactors(riskFactorInfoCommands)
                .patHistoryInfoCommandDiagnoses(diagnosisInfoCommands)
                .listOfPatHis(this.listOfPatHis)
                .listOfSocHis(this.listOfSocHis)
                .listOfFamHis(this.listOfFamHis)
                .build();
    }

    @Override
    public void insertPatHis(String patHis) {
        if (patHis != null && !patHis.trim().equals("") && !this.listOfPatHis.contains(patHis)) {
            this.listOfPatHis.add(patHis);
        }
    }

    @Override
    public void deletePatHis(String patHis) {
        this.listOfPatHis.removeIf(x -> x.equals(patHis));
    }

    @Override
    public void insertSocHis(String socHis) {
        if (socHis != null && !socHis.trim().equals("") && !this.listOfSocHis.contains(socHis)) {
            this.listOfSocHis.add(socHis);
        }
    }

    @Override
    public void deleteSocHis(String socHis) {
        this.listOfSocHis.removeIf(x -> x.equals(socHis));
    }

    @Override
    public void insertFamHis(String famHis) {
        if (famHis != null && !famHis.trim().equals("") && !this.listOfFamHis.contains(famHis)) {
            this.listOfFamHis.add(famHis);
        }
    }

    @Override
    public void deleteFamHis(String famHis) {
        this.listOfFamHis.removeIf(x -> x.equals(famHis));
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
