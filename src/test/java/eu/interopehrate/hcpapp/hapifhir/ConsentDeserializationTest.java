package eu.interopehrate.hcpapp.hapifhir;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.r4.model.Consent;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsentDeserializationTest {
    private static Consent consent;
    private static String consentJSONLocation = "fhir/sample-consent.json";

    @BeforeClass
    public static void beforeClass() throws IOException {
        File fileConsent = new ClassPathResource(consentJSONLocation).getFile();
        String consentAsString = Files.readString(fileConsent.toPath());
        consent = (Consent) FhirContext.forR4().newJsonParser().parseResource(consentAsString);
    }

    @Test
    public void testConsent(){
        String consentAsString = consent.getText().getDiv().toString();
        consentAsString = consentAsString.replaceAll("[<](/)?div[^>]*[>]", "");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(consentAsString);
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

    }


}
