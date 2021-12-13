package eu.interopehrate.hcpapp.services.administration;

import eu.interopehrate.hcpapp.mvc.commands.administration.SEHRInitialDownloadCommand;
import org.hl7.fhir.r4.model.Resource;

import java.util.Iterator;

public interface SEHRInitialDownloadService {
    SEHRInitialDownloadCommand getInitialConfig();
    void saveInitialConfig(SEHRInitialDownloadCommand sehrInitialDownloadCommand);
}
