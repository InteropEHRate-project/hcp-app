package eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary;

import eu.interopehrate.hcpapp.jpa.entities.PrescriptionEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MedicationSummaryPrescriptionCommand {
    private Boolean displayTranslatedVersion;
    private List<MedicationSummaryPrescriptionInfoCommand> medicationSummaryPrescriptionInfoCommand;
    private List<PrescriptionEntity> prescriptionEntities;
}
