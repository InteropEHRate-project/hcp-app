package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AllergyIntoleranceInfoCommand {
    private String identifier;
    @NotEmpty
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    private String clinicalStatus;
    @NotEmpty
    @NotNull
    private String type;
    @NotEmpty
    @NotNull
    private String category;
    @NotEmpty
    @NotNull
    private String criticality;
    @NotEmpty
    @NotNull
    private String code;
}
