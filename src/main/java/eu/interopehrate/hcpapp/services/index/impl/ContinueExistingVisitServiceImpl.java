package eu.interopehrate.hcpapp.services.index.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.services.index.ContinueExistingVisitService;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ContinueExistingVisitServiceImpl implements ContinueExistingVisitService {
    private final RestTemplate restTemplate;
    @Value("${hcp.app.hospital.services.url}")
    private String url;
    private final CurrentPatient currentPatient;

    public ContinueExistingVisitServiceImpl(RestTemplate restTemplate, CurrentPatient currentPatient) {
        this.restTemplate = restTemplate;
        this.currentPatient = currentPatient;
    }

    @Override
    public Patient retrievePatient() {
        return null;
    }

    @Override
    public void retrieveEHRs(Long patientId) {
        List ehrs = this.restTemplate.postForObject(this.url + "/ehrs" + "/list", patientId, List.class);

    }
}
