package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.MedicationSummaryInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.MedicationSummaryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

@Service
public class MedicationSummaryServiceImpl implements MedicationSummaryService {

    @Override
    public List<MedicationSummaryInfoCommand> medicationSummarySection() {
        MedicationSummaryInfoCommand summaryCommand1 = new MedicationSummaryInfoCommand();
        summaryCommand1.setCode("02C01BB");
        summaryCommand1.setInn("Mexiletina");
        summaryCommand1.setManufacturer("Mexitil");
        summaryCommand1.setConcentration("200mg");
        summaryCommand1.setDose("1cps x day");
        summaryCommand1.setStartDate(LocalDate.of(2018, Month.APRIL, 2));
        summaryCommand1.setStatus("Active");

        return Arrays.asList(summaryCommand1);
    }
}
