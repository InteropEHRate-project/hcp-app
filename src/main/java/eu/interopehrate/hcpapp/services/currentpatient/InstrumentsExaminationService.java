package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.InstrumentsExaminationRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.InstrumentsExaminationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.InstrumentsExaminationInfoCommand;

import java.util.List;

public interface InstrumentsExaminationService {
    CurrentPatient getCurrentPatient();
    InstrumentsExaminationCommand instrExam();
    void insertInstrExam(InstrumentsExaminationInfoCommand instrumentsExaminationInfoCommand);
    InstrumentsExaminationInfoCommand retrieveInstrExamById(Long id);
    void updateInstrExam(InstrumentsExaminationInfoCommand instrumentsExaminationInfoCommand);
    void deleteInstrExam(Long id);
    List<InstrumentsExaminationInfoCommand> listNewInstrumentExamination();
    InstrumentsExaminationRepository getInstrumentsExaminationRepository();
}
