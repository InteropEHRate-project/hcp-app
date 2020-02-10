package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergyIntoleranceCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergyIntoleranceInfoCommand;

public interface AllergyIntoleranceService {
    AllergyIntoleranceCommand allergyIntoleranceInfoCommand();

    void insertAllergyIntolerance(AllergyIntoleranceInfoCommand allergyIntoleranceInfoCommand);
}
