package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.jpa.entities.enums.EHRType;
import eu.interopehrate.hcpapp.mvc.models.currentpatient.EHRModel;
import org.hl7.fhir.r4.formats.IParser;
import org.hl7.fhir.r4.formats.JsonParser;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
class SendToOtherHcpServiceImplTest {
    @Value("${hcp.app.hospital.services.url}")
    private String hospitalServicesUrl;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        this.restTemplate = new RestTemplate();
    }

    @Test
    void sendEHRs() throws IOException {
        EHRModel ehrModel = new EHRModel();
        ehrModel.setEhrType(EHRType.IPS);
        ehrModel.setPatientId(1L);

        Bundle content = new Bundle();
        // Serialize Atom Feed
        IParser comp = new JsonParser();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        comp.compose(os, content);
        os.close();
        String json = os.toString();
        ehrModel.setContent(json);

        this.restTemplate.postForLocation(this.hospitalServicesUrl + "/ehrs" + "/transfer", ehrModel);
    }
}