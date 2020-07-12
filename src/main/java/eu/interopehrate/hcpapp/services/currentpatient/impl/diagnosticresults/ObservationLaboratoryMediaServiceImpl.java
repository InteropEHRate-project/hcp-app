package eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults;

import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ObservationLaboratoryMediaService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ObservationLaboratoryMediaServiceImpl implements ObservationLaboratoryMediaService {
    private static final String TMP_FILES_PREFIX = "hcp-app-dicom-";
    private static final String dicomCommand = "$dicom:get -l \"%s\"";
    private static final String weasisCommand = "cmd /c start weasis://%s";

    @Override
    public void displayEcgDemo() {
        String classPathFile = "samples/dicom/ecg/I1";
        this.displayEcg(classPathFile);
    }

    @Override
    public void displayMrDemo() {
        String classPathFile = "samples/dicom/mr/I0";
        this.displayEcg(classPathFile);
    }

    private void displayEcg(String classPathFile) {
        try {
            Path path = Files.createTempFile(TMP_FILES_PREFIX, null);
            path.toFile().deleteOnExit();
            Files.write(path, new ClassPathResource(classPathFile).getInputStream().readAllBytes());

            String encodedDicomCommand = URLEncoder.encode(String.format(dicomCommand, path.toString()), StandardCharsets.UTF_8.toString());
            Runtime.getRuntime().exec(String.format(weasisCommand, encodedDicomCommand));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
