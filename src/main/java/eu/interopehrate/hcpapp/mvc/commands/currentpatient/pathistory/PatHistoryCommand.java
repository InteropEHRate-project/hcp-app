package eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PatHistoryCommand {
    private final Boolean displayTranslatedVersion;
    private final List<PatHistoryInfoCommandDiagnosis> patHistoryInfoCommandDiagnoses;
    private final List<PatHistoryInfoCommandRiskFactor> patHistoryInfoCommandRiskFactors;
    private final List<String> listOfPatHis;
    private final List<String> listOfSocHis;
    private final List<String> listOfFamHis;
}
