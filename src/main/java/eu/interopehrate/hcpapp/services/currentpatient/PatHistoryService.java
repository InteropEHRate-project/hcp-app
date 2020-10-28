package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryCommand;

public interface PatHistoryService {
    PatHistoryCommand patHistorySection();

    void insertPatHis(String patHis);

    void deletePatHis(String patHis);

    void insertSocHis(String socHis);

    void deleteSocHis(String socHis);

    void insertFamHis(String famHis);

    void deleteFamHis(String famHis);
}
