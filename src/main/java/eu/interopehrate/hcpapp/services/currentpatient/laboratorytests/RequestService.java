package eu.interopehrate.hcpapp.services.currentpatient.laboratorytests;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.RequestCommand;

public interface RequestService {
    RequestCommand requestInfoCommand(String id);
}
