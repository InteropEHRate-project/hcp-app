package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergyIntoleranceInfoCommand;

import java.util.List;

public interface AllergyIntoleranceService {
    List<AllergyIntoleranceInfoCommand> allergiesIntolerancesSection();
}
