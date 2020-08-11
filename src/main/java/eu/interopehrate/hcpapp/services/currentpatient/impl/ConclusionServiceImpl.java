package eu.interopehrate.hcpapp.services.currentpatient.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandConclusion;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ConclusionCommand;
import eu.interopehrate.hcpapp.services.currentpatient.ConclusionService;
import lombok.SneakyThrows;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Media;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.stream.Collectors;

@Service
public class ConclusionServiceImpl implements ConclusionService {
    private Bundle imageReport;
    private HapiToCommandConclusion hapiToCommandConclusion;

    @SneakyThrows
    public ConclusionServiceImpl(HapiToCommandConclusion hapiToCommandConclusion) {
        this.hapiToCommandConclusion = hapiToCommandConclusion;

        File json = new ClassPathResource("Medical_Image_V1.json").getFile();
        FileInputStream file = new FileInputStream(json);
        String lineReadtest = readFromInputStream(file);
        IParser parser = FhirContext.forR4().newJsonParser();
        this.imageReport = parser.parseResource(Bundle.class, lineReadtest);
    }

    @Override
    public ConclusionCommand conclusionCommand() {
        var mediaList = imageReport.getEntry()
                .stream()
                .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.Media))
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Media.class::cast)
                .collect(Collectors.toList());

        var imagesInfoCommands = mediaList
                .stream()
                .map(this.hapiToCommandConclusion::convert)
                .collect(Collectors.toList());

        return ConclusionCommand.builder()
                .conclusionInfoCommands(imagesInfoCommands)
                .build();
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
