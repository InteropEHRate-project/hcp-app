package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.MedicationSummaryInfoCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.medicationsummary.MedicationSummaryCommand;
import eu.interopehrate.hcpapp.services.currentpatient.MedicationSummaryService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MedicationSummaryServiceImpl implements MedicationSummaryService {
    private final CurrentPatient currentPatient;
    private List<MedicationSummaryInfoCommand> medicationSummaryInfoCommandList = new ArrayList<>();

    public MedicationSummaryServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public MedicationSummaryCommand statementCommand() {
        return MedicationSummaryCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .statementList(Collections.emptyList())
                .build();
    }

    @Override
    public MedicationSummaryCommand medicationCommand() {
        return MedicationSummaryCommand.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .medicationList(Collections.emptyList())
                .build();
    }

    @Override
    public List<MedicationSummaryInfoCommand> medicationSummarySection() {
        return medicationSummaryInfoCommandList;
    }

    @Override
    public void insertMedicationSummary(MedicationSummaryInfoCommand medicationSummaryInfoCommand) {
        medicationSummaryInfoCommandList.add(medicationSummaryInfoCommand);
    }

    @PostConstruct
    private void postConstruct() {
        MedicationSummaryInfoCommand summaryCommand = new MedicationSummaryInfoCommand();
        summaryCommand.setCode("02C01BB");
        summaryCommand.setInn("Mexiletina");
        summaryCommand.setManufacturer("Mexitil");
        summaryCommand.setConcentration("200mg");
        summaryCommand.setDose("1cps x day");
        summaryCommand.setStartDate(LocalDate.of(2018, Month.APRIL, 2));
        summaryCommand.setStatus("Active");

        medicationSummaryInfoCommandList.add(summaryCommand);
    }
}
