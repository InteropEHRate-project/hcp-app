package eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AllergyInfoCommand {
    private Long id;
    private String idFHIR;
    private String patientId;
    private String author;
    private String identifier;
    @NotEmpty
    @NotNull
    private String category;
    @NotEmpty
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    private String type;
    @NotEmpty
    @NotNull
    private String symptoms;
    private String comments;
}
