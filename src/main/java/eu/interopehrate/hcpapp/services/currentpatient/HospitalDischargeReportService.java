package eu.interopehrate.hcpapp.services.currentpatient;


import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;

public interface HospitalDischargeReportService {
    HospitalDischargeReportCommand hospitalDischargeReportCommand();
    void insertReason(String reason);
    void deleteReason(String reason);
    void insertFinding(String findings);
    void deleteFinding(String finding);
    void insertProcedure(String procedure);
    void deleteProcedure(String procedure);
    void insertCondition(String condition);
    void deleteCondition(String condition);
    void insertInstruction(String instruction);
    void deleteInstruction(String instruction);
}
