package eu.interopehrate.hcpapp.services.currentpatient;



import eu.interopehrate.hcpapp.mvc.commands.currentpatient.ProblemsInfoCommand;

import java.util.List;

public interface ProblemsService {
    List<ProblemsInfoCommand> problemsSection();

    void insertProblem(ProblemsInfoCommand problemsInfoCommand);

}
