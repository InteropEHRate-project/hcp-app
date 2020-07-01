package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ObservationLaboratoryInfoCommandAnalysis {
    @NotEmpty
    @NotNull
    private String analysis = "Default Analysis";

    @NotEmpty
    @NotNull
    private ObservationLaboratoryInfoCommandSample observationLaboratoryInfoCommandSample = new ObservationLaboratoryInfoCommandSample();

    @NotEmpty
    @NotNull
    private Boolean isInLimits = false;


    public void setIsInLimits(){
        if((this.getObservationLaboratoryInfoCommandSample().getCurrentValue()>=this.getObservationLaboratoryInfoCommandSample().getLowerLimitBound()) && (this.getObservationLaboratoryInfoCommandSample().getCurrentValue()<=this.getObservationLaboratoryInfoCommandSample().getUpperLimitBound())){
            isInLimits = true;
        }else{
            isInLimits=false;
        }
    }
}
