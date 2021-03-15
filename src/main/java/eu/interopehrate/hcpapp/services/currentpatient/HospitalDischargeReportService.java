package eu.interopehrate.hcpapp.services.currentpatient;


import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;

public interface HospitalDischargeReportService {
    HospitalDischargeReportCommand hospitalDischargeReportCommand();
    void insertReason(String reason);
    void deleteReason(String reason);
}
