package eu.interopehrate.hcpapp.services.currentpatient.medicationsummary;


import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationCommand;

import java.time.LocalDate;

public interface MedicationService {
    MedicationCommand medicationCommand(String id, String drug, String status, String notes, String timings, String drugDosage, LocalDate dateOfPrescription, LocalDate start, LocalDate end);
}
