package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging.ImageCommand;

import java.io.FileNotFoundException;

public interface DiagnosticImagingService {
    ImageCommand imageCommand();
    void displayEcgDemo();
    void displayMrDemo();
    void refreshData();
    void refresh();
    String downloadMediaFile(String base64, String mediaId, String type) throws FileNotFoundException;
}
