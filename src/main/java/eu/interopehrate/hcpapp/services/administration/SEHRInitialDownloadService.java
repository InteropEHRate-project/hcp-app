package eu.interopehrate.hcpapp.services.administration;

import eu.interopehrate.hcpapp.mvc.commands.administration.SEHRInitialDownloadCommand;

public interface SEHRInitialDownloadService {
    SEHRInitialDownloadCommand getInitialConfig();

    void saveInitialConfig(SEHRInitialDownloadCommand sehrInitialDownloadCommand);
}
