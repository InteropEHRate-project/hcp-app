package eu.interopehrate.hcpapp.services.currentpatient;


import eu.interopehrate.hcpapp.jpa.repositories.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;

public interface HospitalDischargeReportService {
    PrescriptionRepository getPrescriptionRepository();
    VitalSignsRepository getVitalSignsRepository();
    HospitalDischargeReportCommand hospitalDischargeReportCommand();
    void insertDetails(HospitalDischargeReportCommand hospitalDischargeReportCommand);
}
