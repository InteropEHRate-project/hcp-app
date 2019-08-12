package eu.interopehrate.hcpapp.hapifhir;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonDeserializationTests {
    private static String initilaJsonFhir;

    @BeforeClass
    public static void init() throws IOException {
        File file = new ClassPathResource("fhir/sample-patient-summary.json").getFile();
        initilaJsonFhir = Files.readString(file.toPath());
    }

    @Test
    public void testDeserialize() {
        FhirContext fc = FhirContext.forR4();
        IParser parser = fc.newJsonParser().setPrettyPrint(true);

        Bundle patientSummaryBundle = (Bundle) parser.parseResource(initilaJsonFhir);
        String jsonFhir = parser.encodeResourceToString(patientSummaryBundle);

        Assert.assertEquals(StringUtils.trimAllWhitespace(initilaJsonFhir), StringUtils.trimAllWhitespace(jsonFhir));
    }
}
