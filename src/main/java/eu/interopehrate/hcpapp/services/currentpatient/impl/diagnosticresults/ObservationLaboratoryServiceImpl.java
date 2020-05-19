package eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandSample;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommandSample;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ObservationLaboratoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ObservationLaboratoryServiceImpl implements ObservationLaboratoryService {
    private CurrentPatient currentPatient;
    //Hardcoded rows in Observation Laboratory result tabel
    private ObservationLaboratoryInfoCommandAnalysis observationLaboratoryInfoCommandAnalysis1 = new ObservationLaboratoryInfoCommandAnalysis();
    private ObservationLaboratoryInfoCommandAnalysis observationLaboratoryInfoCommandAnalysis2 = new ObservationLaboratoryInfoCommandAnalysis();
    private ObservationLaboratoryInfoCommandAnalysis observationLaboratoryInfoCommandAnalysis3 = new ObservationLaboratoryInfoCommandAnalysis();
    //Hardcoded columns in Observation Laboratory result tabel
    private ObservationLaboratoryInfoCommandSample observationLaboratoryInfoCommandSample1 = new ObservationLaboratoryInfoCommandSample();
    private ObservationLaboratoryInfoCommandSample observationLaboratoryInfoCommandSample2 = new ObservationLaboratoryInfoCommandSample();
    private ObservationLaboratoryInfoCommandSample observationLaboratoryInfoCommandSample3 = new ObservationLaboratoryInfoCommandSample();

    public ObservationLaboratoryServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public ObservationLaboratoryCommandAnalysis observationLaboratoryInfoCommandAnalysis() {
        List<ObservationLaboratoryInfoCommandAnalysis> observationLaboratoryInfoCommandAnalyses = new ArrayList<>();
        observationLaboratoryInfoCommandAnalyses.add(this.observationLaboratoryInfoCommandAnalysis1);

        this.observationLaboratoryInfoCommandAnalysis2.setAnalysis("Eritrociti");
        observationLaboratoryInfoCommandAnalyses.add(this.observationLaboratoryInfoCommandAnalysis2);

        this.observationLaboratoryInfoCommandAnalysis3.setAnalysis("Emoglobina");
        observationLaboratoryInfoCommandAnalyses.add(this.observationLaboratoryInfoCommandAnalysis3);
        return ObservationLaboratoryCommandAnalysis.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .observationLaboratoryInfoCommandAnalyses(observationLaboratoryInfoCommandAnalyses).build();
    }

    @Override
    public ObservationLaboratoryCommandSample observationLaboratoryInfoCommandSample() {
        List<ObservationLaboratoryInfoCommandSample> observationLaboratoryInfoCommandSamples = new ArrayList<>();
        observationLaboratoryInfoCommandSamples.add(this.observationLaboratoryInfoCommandSample1);

        this.observationLaboratoryInfoCommandSample2.setSample(LocalDateTime.now().minusDays(1));
        observationLaboratoryInfoCommandSamples.add(this.observationLaboratoryInfoCommandSample2);

        this.observationLaboratoryInfoCommandSample3.setSample(this.observationLaboratoryInfoCommandSample2.getSample().plusDays(1));
        observationLaboratoryInfoCommandSamples.add(this.observationLaboratoryInfoCommandSample3);
        return ObservationLaboratoryCommandSample.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .observationLaboratoryInfoCommandSamples(observationLaboratoryInfoCommandSamples).build();
    }
}
