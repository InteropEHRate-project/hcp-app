package eu.interopehrate.hcpapp.services.testd2dlibrary;

import eu.interopehrate.hcpapp.mvc.commands.TestD2DLibraryCommand;

public interface TestD2DLibraryService {
    TestD2DLibraryCommand currentState();

    void openConnection() throws Exception;

    void closeConnection() throws Exception;

    void sendMessageToSEHR() throws Exception;

    void lastSEHRMessage();
}
