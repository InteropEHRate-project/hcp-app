package eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults;

import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ObservationLaboratoryMediaService;
import org.springframework.stereotype.Service;

@Service
public class ObservationLaboratoryMediaServiceImpl implements ObservationLaboratoryMediaService {

    @Override
    public void displayEcgDemo() {
        throw new RuntimeException("displayEcgDemo");
    }

    @Override
    public void displayMrDemo() {
        throw new RuntimeException("displayMrDemo");
    }
}
