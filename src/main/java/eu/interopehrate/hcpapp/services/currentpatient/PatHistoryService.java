package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryInfoCommandDiagnosis;

public interface PatHistoryService {
    PatHistoryCommand patHistorySection();
    void insertPatHis(String patHis);
    void deletePatHis(String patHis);
    void insertSocHis(String socHis);
    void deleteSocHis(String socHis);
    void insertFamHis(String famHis);
    void deleteFamHis(String famHis);
    void editRisk(Boolean value, String id);
    PatHistoryInfoCommandDiagnosis patHisInfoCommandById(String id);
    void updateDiagnosis(PatHistoryInfoCommandDiagnosis patHisInfoCommand);
    void deleteDiagnosis(String id);
    void refresh();
}
