package eu.interopehrate.hcpapp.services.currentpatient;


import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseInfoCommand;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;

import java.io.IOException;
import java.util.List;

public interface CurrentDiseaseService {
    CurrentPatient getCurrentPatient();
    CurrentD2DConnection getCurrentD2DConnection();
    CurrentDiseaseCommand currentDiseasesSection();
    void insertCurrentDisease(CurrentDiseaseInfoCommand currentDiseaseCommand);
    void insertNote(String note);
    void deleteNote(String note);
    List<CurrentDiseaseInfoCommand> listNewCurrentDiseases();
    CurrentDiseaseInfoCommand currentDiseaseById(String id);
    void updateCurrentDisease(CurrentDiseaseInfoCommand currentDiseaseInfoCommand);
    void deleteCurrentDisease(String id);
    void deleteNewCurrentDisease(Long id);
    void updateNewCurrentDisease(CurrentDiseaseInfoCommand currentDiseaseInfoCommand);
    CurrentDiseaseInfoCommand retrieveNewCurrentDiseaseById(Long id);
    void refresh();
    Condition callSendCurrentDiseases() throws IOException;
    void sendCurrentDiseases(Bundle condition) throws IOException;
}
