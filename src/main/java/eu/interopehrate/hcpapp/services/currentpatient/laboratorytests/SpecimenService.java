package eu.interopehrate.hcpapp.services.currentpatient.laboratorytests;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.SpecimenCommand;

public interface SpecimenService {
    SpecimenCommand specimenInfoCommand(String id);
}
