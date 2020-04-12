package eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults;

import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.RequestCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.RequestInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.RequestService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    private CurrentPatient currentPatient;
    //Hardcoded Request
    private RequestInfoCommand requestInfoCommand = new RequestInfoCommand();

    public RequestServiceImpl(CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
    }

    @Override
    public RequestCommand requestInfoCommand(String id) {
        List<RequestInfoCommand> requestInfoCommands = new ArrayList<>();
        requestInfoCommand.setId(id);
        requestInfoCommands.add(requestInfoCommand);
        return RequestCommand.builder().displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .requestInfoCommandList(requestInfoCommands)
                .build();
    }
}
