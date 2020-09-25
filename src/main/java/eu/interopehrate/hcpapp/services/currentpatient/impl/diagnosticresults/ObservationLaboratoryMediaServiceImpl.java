package eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.converters.fhir.diagnosticimaging.HapiToCommandImage;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging.ImageCommand;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ObservationLaboratoryMediaService;
import lombok.SneakyThrows;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Media;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Service
public class ObservationLaboratoryMediaServiceImpl implements ObservationLaboratoryMediaService {
    private Bundle imageReport;
    private HapiToCommandImage hapiToCommandImage;
    private static final String TMP_FILES_PREFIX = "hcp-app-dicom-";
    private static final String dicomCommand = "$dicom:get -l \"%s\"";
    private static final String weasisCommand = "cmd /c start weasis://%s";

    @SneakyThrows
    public ObservationLaboratoryMediaServiceImpl(HapiToCommandImage hapiToCommandImage) {
        this.hapiToCommandImage = hapiToCommandImage;

        File json = new ClassPathResource("Medical_Image_V1.json").getFile();
        FileInputStream file = new FileInputStream(json);
        String lineReadtest = readFromInputStream(file);
        IParser parser = FhirContext.forR4().newJsonParser();
        this.imageReport = parser.parseResource(Bundle.class, lineReadtest);
    }

    @Override
    public ImageCommand imageCommand() {
        var mediaList = imageReport.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.Media))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Media.class::cast)
                .collect(Collectors.toList());

        var imagesInfoCommands = mediaList
                .stream()
                .map(this.hapiToCommandImage::convert)
                .collect(Collectors.toList());

        return ImageCommand.builder()
                .imageInfoCommands(imagesInfoCommands)
                .build();
    }

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

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
