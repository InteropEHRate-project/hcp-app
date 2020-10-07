package eu.interopehrate.hcpapp.services.currentpatient.impl.laboratorytests;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ResultCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ResultInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.laboratorytests.ResultService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ResultServiceImpl implements ResultService {
    private CurrentPatient currentPatient;
    //Hardcoded Result
    private ResultInfoCommand resultInfoCommand = new ResultInfoCommand();

    public ResultServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public ResultCommand resultInfoCommand(String id) {
        List<ResultInfoCommand> resultInfoCommands = new ArrayList<>();
        resultInfoCommand.setId(id);
        resultInfoCommands.add(resultInfoCommand);
        return ResultCommand.builder().displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .resultInfoCommandList(resultInfoCommands)
                .build();
    }
}
