package eu.interopehrate.hcpapp.services.currentpatient;


import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ProblemCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ProblemInfoCommand;

public interface ProblemService {
    ProblemCommand problemsSection();

    void insertProblem(ProblemInfoCommand problemInfoCommand);
}
