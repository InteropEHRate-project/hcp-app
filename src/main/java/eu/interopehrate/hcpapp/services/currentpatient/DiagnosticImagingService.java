package eu.interopehrate.hcpapp.services.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging.ImageCommand;

public interface DiagnosticImagingService {
    ImageCommand imageCommand();

    void displayEcgDemo();

    void displayMrDemo();
}
