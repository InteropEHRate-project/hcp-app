package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;
import eu.interopehrate.hcpapp.services.currentpatient.HospitalDischargeReportService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalDischargeReportServiceImpl implements HospitalDischargeReportService {
    private final List<String> listOfReasons = new ArrayList<>();

    @Override
    public HospitalDischargeReportCommand hospitalDischargeReportCommand() {
        return HospitalDischargeReportCommand.builder()
                .listOfReasons(this.listOfReasons)
                .build();
    }

    @Override
    public void insertReason(String reason) {
        if (reason != null && !reason.trim().equals("") && !this.listOfReasons.contains(reason)) {
            this.listOfReasons.add(reason);
        }
    }

    @Override
    public void deleteReason(String reason) {
        this.listOfReasons.removeIf(x -> x.equals(reason));
    }
}
