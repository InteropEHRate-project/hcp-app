package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.HospitalDischargeReportCommand;
import eu.interopehrate.hcpapp.services.currentpatient.HospitalDischargeReportService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HospitalDischargeReportServiceImpl implements HospitalDischargeReportService {
    private final List<String> listOfReasons = new ArrayList<>();
    private final List<String> listOfFindings = new ArrayList<>();
    private final List<String> listOfProcedures = new ArrayList<>();
    private final List<String> listOfConditions = new ArrayList<>();
    private final List<String> listOfInstructions = new ArrayList<>();

    @Override
    public HospitalDischargeReportCommand hospitalDischargeReportCommand() {
        return HospitalDischargeReportCommand.builder()
                .listOfReasons(this.listOfReasons)
                .listOfFindings(this.listOfFindings)
                .listOfProcedures(this.listOfProcedures)
                .listOfConditions(this.listOfConditions)
                .listOfInstructions(this.listOfInstructions)
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

    @Override
    public void insertFinding(String findings) {
        if (findings != null && !findings.trim().equals("") && !this.listOfFindings.contains(findings)) {
            this.listOfFindings.add(findings);
        }
    }

    @Override
    public void deleteFinding(String finding) {
        this.listOfFindings.removeIf(x -> x.equals(finding));
    }

    @Override
    public void insertProcedure(String procedure) {
        if (procedure != null && !procedure.trim().equals("") && !this.listOfProcedures.contains(procedure)) {
            this.listOfProcedures.add(procedure);
        }
    }

    @Override
    public void deleteProcedure(String procedure) {
        this.listOfProcedures.removeIf(x -> x.equals(procedure));
    }

    @Override
    public void insertCondition(String condition) {
        if (condition != null && !condition.trim().equals("") && !this.listOfConditions.contains(condition)) {
            this.listOfConditions.add(condition);
        }
    }

    @Override
    public void deleteCondition(String condition) {
        this.listOfConditions.removeIf(x -> x.equals(condition));
    }

    @Override
    public void insertInstruction(String instruction) {
        if (instruction != null && !instruction.trim().equals("") && !this.listOfInstructions.contains(instruction)) {
            this.listOfInstructions.add(instruction);
        }
    }

    @Override
    public void deleteInstruction(String instruction) {
        this.listOfInstructions.removeIf(x -> x.equals(instruction));
    }
}
