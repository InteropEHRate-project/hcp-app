package eu.interopehrate.hcpapp.services.index;

import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;

public interface IndexService {
    IndexCommand indexCommand() throws Exception;

    void openConnection();

    void closeConnection();

    void certificate();
}
