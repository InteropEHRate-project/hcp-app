package eu.interopehrate.hcpapp.services.index;

import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;

public interface IndexService {
    IndexCommand d2dConnectionState() throws Exception;

    void openConnection();

    void closeConnection();
}
