package eu.interopehrate.hcpapp.services.currentpatient;


import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.CurrentDiseaseInfoCommand;

public interface CurrentDiseaseService {
    CurrentDiseaseCommand currentDiseasesSection();

    void insertCurrentDisease(CurrentDiseaseInfoCommand currentDiseaseCommand);
}
