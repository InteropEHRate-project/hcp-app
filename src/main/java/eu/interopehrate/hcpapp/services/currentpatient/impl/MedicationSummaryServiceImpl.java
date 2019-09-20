package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.MedicationSummaryInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.MedicationSummaryService;
import org.springframework.stereotype.Service;

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
        summaryCommand1.setStartDate("16/10/2016");
        summaryCommand1.setStatus("Active");

        MedicationSummaryInfoCommand summaryCommand2 = new MedicationSummaryInfoCommand();
        summaryCommand2.setCode("02C01BB");
        summaryCommand2.setInn("Mexiletina");
        summaryCommand2.setManufacturer("Mexitil");
        summaryCommand2.setConcentration("100mg");
        summaryCommand2.setDose("1cps x day");
        summaryCommand2.setStartDate("16/10/2017");
        summaryCommand2.setStatus("Active");

        return Arrays.asList(summaryCommand1, summaryCommand2);
    }
}
