package eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PatHistoryCommand {
    private Boolean displayTranslatedVersion;
    private List<PatHistoryInfoCommandDiagnosis> patHistoryInfoCommandDiagnoses;
    private List<PatHistoryInfoCommandRiskFactor> patHistoryInfoCommandRiskFactors;
    private List<String> listOfPatHis;
    private List<String> listOfSocHis;
    private List<String> listOfFamHis;
}
