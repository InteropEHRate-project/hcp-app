package eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging.ImageCommand;

public interface ObservationLaboratoryMediaService {

    ImageCommand imageCommand();

    void displayEcgDemo();

    void displayMrDemo();
}
