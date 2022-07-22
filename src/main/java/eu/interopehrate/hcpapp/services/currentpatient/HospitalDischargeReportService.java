package eu.interopehrate.hcpapp.services.currentpatient;


import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.AllergyRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.CurrentDiseaseRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.LaboratoryTestsRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.PrescriptionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.DiagnosticConclusionRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.PHExamRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.ReasonRepository;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.VitalSignsRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;

public interface HospitalDischargeReportService {
    CurrentDiseaseRepository getCurrentDiseaseRepository();
    AllergyRepository getAllergyRepository();
    PrescriptionRepository getPrescriptionRepository();
    VitalSignsRepository getVitalSignsRepository();
    DiagnosticConclusionRepository getDiagnosticConclusionRepository();
    HospitalDischargeReportCommand hospitalDischargeReportCommand();
    void insertDetails(HospitalDischargeReportCommand hospitalDischargeReportCommand);
    Boolean saveInCloud(byte[] content);
    PHExamRepository getPhExamRepository();
    LaboratoryTestsRepository getLaboratoryTestsRepository();
    ReasonRepository getReasonRepository();
}
