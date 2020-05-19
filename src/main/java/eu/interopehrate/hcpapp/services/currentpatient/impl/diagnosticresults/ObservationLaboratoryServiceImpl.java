package eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandAnalyte;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandSample;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommandAnalyte;
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
    private ObservationLaboratoryInfoCommandAnalyte observationLaboratoryInfoCommandAnalyte1 = new ObservationLaboratoryInfoCommandAnalyte();
    private ObservationLaboratoryInfoCommandAnalyte observationLaboratoryInfoCommandAnalyte2 = new ObservationLaboratoryInfoCommandAnalyte();
    private ObservationLaboratoryInfoCommandAnalyte observationLaboratoryInfoCommandAnalyte3 = new ObservationLaboratoryInfoCommandAnalyte();
    //Hardcoded columns in Observation Laboratory result tabel
    private ObservationLaboratoryInfoCommandSample observationLaboratoryInfoCommandSample1 = new ObservationLaboratoryInfoCommandSample();
    private ObservationLaboratoryInfoCommandSample observationLaboratoryInfoCommandSample2 = new ObservationLaboratoryInfoCommandSample();
    private ObservationLaboratoryInfoCommandSample observationLaboratoryInfoCommandSample3 = new ObservationLaboratoryInfoCommandSample();

    public ObservationLaboratoryServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public ObservationLaboratoryCommandAnalyte observationLaboratoryInfoCommand() {
        List<ObservationLaboratoryInfoCommandAnalyte> observationLaboratoryInfoCommandAnalytes = new ArrayList<>();
        observationLaboratoryInfoCommandAnalytes.add(this.observationLaboratoryInfoCommandAnalyte1);

        this.observationLaboratoryInfoCommandAnalyte2.setAnalyte("Eritrociti");
        observationLaboratoryInfoCommandAnalytes.add(this.observationLaboratoryInfoCommandAnalyte2);

        this.observationLaboratoryInfoCommandAnalyte3.setAnalyte("Emoglobina");
        observationLaboratoryInfoCommandAnalytes.add(this.observationLaboratoryInfoCommandAnalyte3);
        return ObservationLaboratoryCommandAnalyte.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .observationLaboratoryInfoCommandAnalytes(observationLaboratoryInfoCommandAnalytes).build();
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
