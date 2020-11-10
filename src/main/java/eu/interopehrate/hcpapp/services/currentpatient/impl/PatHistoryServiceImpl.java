package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.fhir.pathistory.HapiToCommandDiagnosis;
import eu.interopehrate.hcpapp.converters.fhir.pathistory.HapiToCommandRiskFactor;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryCommand;
import eu.interopehrate.hcpapp.services.currentpatient.PatHistoryService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatHistoryServiceImpl implements PatHistoryService {
    private CurrentPatient currentPatient;
    private final List<String> listOfPatHis = new ArrayList<>();
    private final List<String> listOfSocHis = new ArrayList<>();
    private final List<String> listOfFamHis = new ArrayList<>();
    private final HapiToCommandRiskFactor hapiToCommandRiskFactor;
    private final HapiToCommandDiagnosis hapiToCommandDiagnosis;

    @SneakyThrows
    public PatHistoryServiceImpl(CurrentPatient currentPatient, HapiToCommandRiskFactor hapiToCommandRiskFactor, HapiToCommandDiagnosis hapiToCommandDiagnosis) {
        this.currentPatient = currentPatient;
        this.hapiToCommandRiskFactor = hapiToCommandRiskFactor;
        this.hapiToCommandDiagnosis = hapiToCommandDiagnosis;
    }

    @Override
    public PatHistoryCommand patHistorySection() {
        var riskFactorInfoCommands = this.currentPatient.patHisConsultationRiskFactorsList()
                .stream()
                .map(hapiToCommandRiskFactor::convert)
                .collect(Collectors.toList());
        var diagnosisInfoCommands = this.currentPatient.patHisConsultationDiagnosesList()
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
}
