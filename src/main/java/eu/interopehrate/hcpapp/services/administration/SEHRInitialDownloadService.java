package eu.interopehrate.hcpapp.services.administration;

import eu.interopehrate.hcpapp.mvc.commands.SEHRInitialDownloadCommand;

public interface SEHRInitialDownloadService {
    SEHRInitialDownloadCommand getInitialConfig();
}
