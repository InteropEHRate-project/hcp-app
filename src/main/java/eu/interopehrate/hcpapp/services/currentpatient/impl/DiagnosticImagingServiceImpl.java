package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.fhir.diagnosticimaging.HapiToCommandImage;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging.ImageCommand;
import eu.interopehrate.hcpapp.services.currentpatient.DiagnosticImagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DiagnosticImagingServiceImpl implements DiagnosticImagingService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandImage hapiToCommandImage;
    private static final String TMP_FILES_PREFIX = "hcp-app-dicom-";
    private static final String dicomCommand = "$dicom:get -l \"%s\"";
    private static final String weasisCommand = "cmd /c start weasis://%s";

    public DiagnosticImagingServiceImpl(CurrentPatient currentPatient, HapiToCommandImage hapiToCommandImage) {
        this.currentPatient = currentPatient;
        this.hapiToCommandImage = hapiToCommandImage;
    }

    @Override
    public ImageCommand imageCommand() {
        var imageInfoCommands = currentPatient.mediaList()
                .stream()
                .map(hapiToCommandImage::convert)
                .collect(Collectors.toList());
        return ImageCommand.builder()
                .imageInfoCommands(imageInfoCommands)
                .build();
    }

    @Override
    public void displayEcgDemo() {
        String classPathFile = "samples/dicom/ecg/I1";
        displayEcg(classPathFile);
    }

    @Override
    public void displayMrDemo() {
        String classPathFile = "samples/dicom/mr/I0";
        displayEcg(classPathFile);
    }

    private static void displayEcg(String classPathFile) {
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
