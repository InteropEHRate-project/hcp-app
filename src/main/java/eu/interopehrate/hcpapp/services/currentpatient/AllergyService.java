package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyInfoCommand;

import java.util.List;

public interface AllergyService {
    CurrentPatient getCurrentPatient();
    AllergyCommand allergyInfoCommand();
    AllergyCommand allergyInfoCommandTranslated();
    void insertAllergy(AllergyInfoCommand allergyInfoCommand);
    List<AllergyInfoCommand> listOfNewAllergies();
    void deleteNewAllergy(Long id);
}
