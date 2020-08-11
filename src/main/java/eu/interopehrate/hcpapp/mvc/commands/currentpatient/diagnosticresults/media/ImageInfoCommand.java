package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.media;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageInfoCommand {
    private String name;
    private String imageType;
    private String imageContent;
    private int size;

    private String completeStringForImageDisplaying;
}
