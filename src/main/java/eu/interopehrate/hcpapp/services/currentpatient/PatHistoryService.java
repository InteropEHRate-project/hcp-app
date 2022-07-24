package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PatHistoryRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryInfoCommandDiagnosis;
import org.hl7.fhir.r4.model.Condition;

import java.util.List;

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
    void insertPathHistory(PatHistoryInfoCommandDiagnosis patHistoryInfoCommandDiagnosis);
    List<PatHistoryInfoCommandDiagnosis> getNewPat();
    PatHistoryCommand patHistoryCommand();
    Condition callPatHis();
    CurrentPatient getCurrentPatient();
    CurrentD2DConnection getCurrentD2DConnection();
    PatHistoryRepository getPatHistoryRepository();
}
