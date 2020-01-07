package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.services.currentpatient.CurrentPatientService;
import org.springframework.stereotype.Service;

@Service
public class CurrentPatientServiceImpl implements CurrentPatientService {
    private CurrentPatient currentPatient;

    public CurrentPatientServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public void setDisplayTranslatedVersion(Boolean displayTranslatedVersion) {
        currentPatient.setDisplayTranslatedVersion(displayTranslatedVersion);
    }

    @Override
    public Boolean getDisplayTranslatedVersion() {
        return currentPatient.getDisplayTranslatedVersion();
    }
}
