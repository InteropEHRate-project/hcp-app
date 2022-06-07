package eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class AllergyInfoCommand {
    private Long id;
    private String idFHIR;
    private String patientId;
    private String author;
    private String identifier;
    private String category;
    @NotEmpty
    @NotNull
    private String name;
    private String nameTranslated = "";
    @NotEmpty
    @NotNull
    private String type;
    private String symptoms;
    private String comments;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate endDate;
}
