package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyIntoleranceCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyIntoleranceInfoCommand;

public interface AllergyIntoleranceService {
    CurrentPatient getCurrentPatient();
    AllergyIntoleranceCommand allergyIntoleranceInfoCommand();
    AllergyIntoleranceCommand allergyIntoleranceInfoCommandTranslated();
    void insertAllergyIntolerance(AllergyIntoleranceInfoCommand allergyIntoleranceInfoCommand);
}
