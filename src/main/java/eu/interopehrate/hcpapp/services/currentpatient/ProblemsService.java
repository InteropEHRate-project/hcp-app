package eu.interopehrate.hcpapp.services.currentpatient;


import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ProblemsCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ProblemsInfoCommand;

public interface ProblemsService {
    ProblemsCommand problemsSection();

    void insertProblem(ProblemsInfoCommand problemsInfoCommand);
}
