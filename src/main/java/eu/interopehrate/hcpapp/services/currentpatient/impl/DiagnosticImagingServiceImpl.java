package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.converters.fhir.diagnosticimaging.HapiToCommandDiagnosticReport;
import eu.interopehrate.hcpapp.converters.fhir.diagnosticimaging.HapiToCommandImage;
import eu.interopehrate.hcpapp.currentsession.CloudConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging.ImageCommand;
import eu.interopehrate.hcpapp.services.currentpatient.DiagnosticImagingService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DiagnosticImagingServiceImpl implements DiagnosticImagingService {
    private final CurrentPatient currentPatient;
    private final HapiToCommandImage hapiToCommandImage;
    private final HapiToCommandDiagnosticReport hapiToCommandDiagnosticReport;
    private final CloudConnection cloudConnection;
    private final CurrentD2DConnection currentD2DConnection;
    private static final String TMP_FILES_PREFIX = "hcp-app-dicom-";
    private static final String dicomCommand = "$dicom:get -l \"%s\"";
    private static final String weasisCommand = "cmd /c start weasis://%s";

    public DiagnosticImagingServiceImpl(CurrentPatient currentPatient, HapiToCommandImage hapiToCommandImage,
                                        HapiToCommandDiagnosticReport hapiToCommandDiagnosticReport, CloudConnection cloudConnection, CurrentD2DConnection currentD2DConnection) {
        this.currentPatient = currentPatient;
        this.hapiToCommandImage = hapiToCommandImage;
        this.hapiToCommandDiagnosticReport = hapiToCommandDiagnosticReport;
        this.cloudConnection = cloudConnection;
        this.currentD2DConnection = currentD2DConnection;
    }

    @Override
    public ImageCommand imageCommand() {
        var imageInfoCommands = currentPatient.mediaList()
                .stream()
                .map(this.hapiToCommandImage::convert)
                .collect(Collectors.toList());
        var diagnosticReports = this.currentPatient.diagnosticReportList()
                .stream()
                .filter(dr -> (dr.hasCategory() && !"LAB".equals(dr.getCategoryFirstRep().getCodingFirstRep().getCode())))
                .map(this.hapiToCommandDiagnosticReport::convert)
                .collect(Collectors.toList());
        return ImageCommand.builder()
                .imageInfoCommands(imageInfoCommands)
                .diagnosticReportInfoCommands(diagnosticReports)
                .build();
    }

    @Override
    public void displayEcgDemo() {
        String classPathFile = "samples/dicom/ecg/I1";
        displayEcg(classPathFile);
    }

    @SneakyThrows
    @Override
    public String downloadMediaFile(String base64, String mediaId, String type) {
        byte[] byteArray = Base64.getDecoder().decode(base64);

        String fileName = mediaId.replaceAll("/", "_");
        if ("application/pdf".equals(type)) {
            fileName = fileName + ".pdf";
        } else if ("application/dicom".equals(type)) {
            fileName = fileName + ".dcm";
        } else if ("application/dicom+zip".equals(type)) {
            fileName = fileName + ".zip";
        } else {
            fileName = fileName + ".bin";
        }

        String home = System.getProperty("user.home");
        File f = new File(home + "/" + fileName);
        System.out.println(" Creating file " + f.getAbsolutePath());
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f, false))) {
            out.write(byteArray);
        } catch (Exception e) {
            System.out.println("Couldn't write to file: " + e.getMessage());
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }

    @Override
    public void displayMrDemo() {
        String classPathFile = "samples/dicom/mr/I0";
        displayEcg(classPathFile);
    }

    @Override
    public void refreshData() {
        this.cloudConnection.downloadImageReport();
    }

    @Override
    public void refresh() {
        this.currentD2DConnection.getDiagnosticImaging();
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
