package eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
public class ObservationLaboratoryInfoCommandAnalysis {
    private Long id;
    @NotNull
    @NotEmpty
    private String analysis = "Default Analysis";
    private String analysisTranslated = "";
    private Boolean isInLimits = false;
    private String referenceRange;
    private String author;
    @NotNull
    @NotEmpty
    private String analysisName;
    @NotNull
    private ObservationLaboratoryInfoCommandSample observationLaboratoryInfoCommandSample = new ObservationLaboratoryInfoCommandSample();

    public void setIsInLimits() {
        if (Objects.nonNull(this.getObservationLaboratoryInfoCommandSample().getCurrentValue())) {
            if ((this.getObservationLaboratoryInfoCommandSample().getCurrentValue() >= this.getObservationLaboratoryInfoCommandSample().getLowerLimitBound()) &&
                    (this.getObservationLaboratoryInfoCommandSample().getCurrentValue() <= this.getObservationLaboratoryInfoCommandSample().getUpperLimitBound())) {
                this.isInLimits = true;
            } else {
                this.isInLimits = false;
            }
        } else {
            this.isInLimits = true;
        }
    }
}
