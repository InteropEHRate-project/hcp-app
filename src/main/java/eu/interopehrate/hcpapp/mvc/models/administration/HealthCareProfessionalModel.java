package eu.interopehrate.hcpapp.mvc.models.administration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HealthCareProfessionalModel {
    @JsonProperty("name")
    private String name;
    @JsonProperty("occupation")
    private String occupation;
}
