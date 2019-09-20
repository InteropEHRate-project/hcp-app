package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.AllergiesIntolerancesInfoCommand;

import java.util.List;

public interface AllergiesIntolerancesService {
    List<AllergiesIntolerancesInfoCommand> allergiesIntolerancesSection();
}
