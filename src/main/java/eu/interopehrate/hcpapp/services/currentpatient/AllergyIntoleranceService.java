package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyIntoleranceCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.allergy.AllergyIntoleranceInfoCommand;

public interface AllergyIntoleranceService {
    AllergyIntoleranceCommand allergyIntoleranceInfoCommand();
    void insertAllergyIntolerance(AllergyIntoleranceInfoCommand allergyIntoleranceInfoCommand);
}
