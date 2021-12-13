package eu.interopehrate.hcpapp.services.currentpatient.laboratorytests;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryCommandAnalysis;

public interface ObservationLaboratoryService {
    boolean isFiltered();
    boolean isEmpty();
    ObservationLaboratoryCommandAnalysis observationLaboratoryInfoCommandAnalysis(String keyword);
    void refreshData();
    void getLaboratoryTests() throws Exception;
}
