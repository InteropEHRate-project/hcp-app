package eu.interopehrate.hcpapp.services.emergency;

import eu.interopehrate.hcpapp.mvc.commands.emergency.EmergencyIndexCommand;

public interface EmergencyIndexService {
    EmergencyIndexCommand emergencyIndexCommand() throws Exception;

    void openConnection(String qrCode);

    void closeConnection();
}
