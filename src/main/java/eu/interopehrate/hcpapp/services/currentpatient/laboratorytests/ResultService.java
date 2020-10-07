package eu.interopehrate.hcpapp.services.currentpatient.laboratorytests;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ResultCommand;

public interface ResultService {
    ResultCommand resultInfoCommand(String id);
}
