package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.enums.InstrumentsExamType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.File;
import java.time.LocalDate;

@Getter
@Setter
public class InstrumentsExaminationInfoCommand {
    private Long id;
    private InstrumentsExamType type;
    private String result;
    private String author;
    private File file;
    private String name;
    private String types;
    private byte[] data;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate date;
}
