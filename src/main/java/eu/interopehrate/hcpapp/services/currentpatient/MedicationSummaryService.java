package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.MedicationSummaryInfoCommand;

import java.util.List;

public interface MedicationSummaryService {
    List<MedicationSummaryInfoCommand> medicationSummarySection();
}
