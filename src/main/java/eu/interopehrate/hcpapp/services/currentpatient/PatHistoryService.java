package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.PatHistoryCommand;

public interface PatHistoryService {
    PatHistoryCommand currentDiseasesSection();
}
