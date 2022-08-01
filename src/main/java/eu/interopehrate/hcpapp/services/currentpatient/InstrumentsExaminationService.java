package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata.InstrumentsExaminationEntity;
import eu.interopehrate.hcpapp.jpa.repositories.currentpatient.visitdata.InstrumentsExaminationRepository;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.InstrumentsExaminationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.InstrumentsExaminationInfoCommand;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Media;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    DiagnosticReport callSendInstrumentalExamination() throws IOException;
    void sendInstrumentalExamination(Bundle instrExamination) throws IOException;
    InstrumentsExaminationEntity store(MultipartFile file) throws IOException;
    CurrentD2DConnection getCurrentD2DConnection();
    List<InstrumentsExaminationEntity>getFiles();
    void insertResultNote(String resultNote);
    Media callSendInstrumentalExaminationMedia();
    Media callSendInstrumentalExaminationMediaAnon();
}
