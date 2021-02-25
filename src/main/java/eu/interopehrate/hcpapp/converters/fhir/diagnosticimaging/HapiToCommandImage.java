package eu.interopehrate.hcpapp.converters.fhir.diagnosticimaging;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging.ImageInfoCommand;
import org.hl7.fhir.r4.model.Media;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Base64;

@Component
public class HapiToCommandImage implements Converter<Media, ImageInfoCommand> {
    private final CurrentPatient currentPatient;

    public HapiToCommandImage(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public ImageInfoCommand convert(Media image) {
        ImageInfoCommand imageInfoCommand = new ImageInfoCommand();

        if (image.hasType() && image.getType().hasCoding()) {
            image.getType().getCoding().forEach(coding -> imageInfoCommand.setImageName(CurrentPatient.extractExtensionText(coding, this.currentPatient)));
        }

        if (image.hasContent()) {
            imageInfoCommand.setImageType(image.getContent().getContentType());
            imageInfoCommand.setImageContent(pictureBase64(image.getContent().getData()));
            imageInfoCommand.setSizeOfImage(image.getContent().getSize());

            String imageToDisplay = String.join(",", "data:" + imageInfoCommand.getImageType() + ";base64", imageInfoCommand.getImageContent());
            imageInfoCommand.setCompleteStringForImageDisplaying(imageToDisplay);
        }

        if (image.hasCreatedDateTimeType()) {
            imageInfoCommand.setDateOfImage(image.getCreatedDateTimeType().getValue()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        return imageInfoCommand;
    }

    private static String pictureBase64(byte[] picture) {
        return Base64.getEncoder().encodeToString(picture);
    }
}
