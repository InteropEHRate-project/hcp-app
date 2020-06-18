package eu.interopehrate.hcpapp.mvc.commands.administration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SEHRInitialDownloadCommand {
    private Boolean currentDiseases = Boolean.FALSE;
    private Boolean patHistory = Boolean.FALSE;
    private Boolean allergies = Boolean.FALSE;
    private Boolean currentMedication = Boolean.FALSE;
    private Boolean documentHistoryConsultation = Boolean.FALSE;
    private Boolean laboratoryTests = Boolean.FALSE;
}
