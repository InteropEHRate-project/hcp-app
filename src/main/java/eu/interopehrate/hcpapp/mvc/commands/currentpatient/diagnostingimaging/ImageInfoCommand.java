package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ImageInfoCommand {
    private String name;
    private String imageType;
    private String imageContent;
    private int size;
    private LocalDate dateOfExam;

    private String completeStringForImageDisplaying;
}
