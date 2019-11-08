package eu.interopehrate.hcpapp.mvc.commands.administration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SEHRInitialDownloadCommand {
    private Boolean medicationSummary = Boolean.FALSE;
    private Boolean allergyIntolerance = Boolean.FALSE;
    private Boolean problems = Boolean.FALSE;
    private Boolean historyOfProcedure = Boolean.FALSE;
    private Boolean immunizations = Boolean.FALSE;
    private Boolean medicalDevices = Boolean.FALSE;
    private Boolean results = Boolean.FALSE;
    private Boolean historyOfPast = Boolean.FALSE;
    private Boolean functionalStatus = Boolean.FALSE;
    private Boolean planOfCare = Boolean.FALSE;
    private Boolean socialHistory = Boolean.FALSE;
    private Boolean historyOfPregnancy = Boolean.FALSE;
    private Boolean advancesDirectives = Boolean.FALSE;
}
