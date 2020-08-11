package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConclusionInfoCommand {
    private String name;
    private String imageType;
    private String imageContent;
    private int size;

    private String completeStringForImageDisplaying;
}
