package eu.interopehrate.hcpapp.mvc.commands.currentpatient;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class HospitalDischargeReportCommand {
    private final List<String> listOfReasons;
    private final List<String> listOfFindings;
    private final List<String> listOfProcedures;
    private final List<String> listOfConditions;
    private final List<String> listOfInstructions;
}
