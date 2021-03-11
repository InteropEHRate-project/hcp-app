package eu.interopehrate.hcpapp.mvc.models.currentpatient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferredPatientModel {
    @JsonProperty("patientId")
    private String patientId;
    @JsonProperty("initialHcpId")
    private Long initialHcpId;
    @JsonProperty("hcpId")
    private Long hcpId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("age")
    private Integer age;
    @JsonProperty("country")
    private String country;
}
