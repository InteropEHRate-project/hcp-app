package eu.interopehrate.hcpapp.services.currentpatient.laboratorytests;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging.ImageCommand;

public interface ObservationLaboratoryMediaService {

    ImageCommand imageCommand();

    void displayEcgDemo();

    void displayMrDemo();
}
