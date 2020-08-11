package eu.interopehrate.hcpapp.converters.fhir;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ConclusionInfoCommand;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Media;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class HapiToCommandConclusion implements Converter<Media, ConclusionInfoCommand> {
    private final CurrentPatient currentPatient;

    public HapiToCommandConclusion(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public ConclusionInfoCommand convert(Media image) {
        ConclusionInfoCommand conclusionInfoCommand = new ConclusionInfoCommand();

        if (image.hasType() && image.getType().hasCoding()) {
            for (Coding coding : image.getType().getCoding()) {
                conclusionInfoCommand.setName(CurrentPatient.extractExtensionText(coding, this.currentPatient));
            }
        }

        if (image.hasContent()) {
            conclusionInfoCommand.setImageType(image.getContent().getContentType());
            conclusionInfoCommand.setImageContent(pictureBase64(image.getContent().getData()));
            conclusionInfoCommand.setSize(image.getContent().getSize());

            String imageToDisplay = String.join(",", "data:" + conclusionInfoCommand.getImageType() + ";base64", conclusionInfoCommand.getImageContent());
            conclusionInfoCommand.setCompleteStringForImageDisplaying(imageToDisplay);
        }
        return conclusionInfoCommand;
    }

    private static String pictureBase64(byte[] picture) {
        return Base64.getEncoder().encodeToString(picture);
    }
}
