package eu.interopehrate.hcpapp.services.administration;

import eu.interopehrate.hcpapp.mvc.commands.AllergiesIntolerancesInfoCommand;

import java.util.List;

public interface AllergiesIntolerancesService {
    List<AllergiesIntolerancesInfoCommand> allergiesIntolerancesSection();
}
