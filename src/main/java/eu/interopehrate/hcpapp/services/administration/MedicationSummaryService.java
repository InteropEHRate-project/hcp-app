package eu.interopehrate.hcpapp.services.administration;

import eu.interopehrate.hcpapp.mvc.commands.MedicationSummaryInfoCommand;

import java.util.List;

public interface MedicationSummaryService {
    List<MedicationSummaryInfoCommand> medicationSummarySection();
}
