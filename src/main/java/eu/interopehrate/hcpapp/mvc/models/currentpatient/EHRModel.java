package eu.interopehrate.hcpapp.mvc.models.currentpatient;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.interopehrate.hcpapp.jpa.entities.enums.EHRType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EHRModel {
    @JsonProperty("patientId")
    private Long patientId;
    @JsonProperty("EHR_TYPE")
    private EHRType ehrType;
    @JsonProperty("CONTENT")
    private String content;
}
