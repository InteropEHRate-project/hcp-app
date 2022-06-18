package eu.interopehrate.hcpapp.services.currentpatient.laboratorytests;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.ObservationLaboratoryInfoCommandAnalysis;
import org.hl7.fhir.r4.model.Observation;

import java.util.HashMap;

public interface ObservationLaboratoryService {
    boolean isFiltered();
    boolean isEmpty();
    ObservationLaboratoryCommandAnalysis observationLaboratoryInfoCommandAnalysis(String keyword);
    void refreshData();
    void getLaboratoryTests() throws Exception;
    void insertLaboratory(ObservationLaboratoryInfoCommandAnalysis observationLaboratoryInfoCommandAnalysis);
    CurrentPatient getCurrentPatient();
    @SuppressWarnings("rawtypes")
    HashMap correlations();
    ObservationLaboratoryCommandAnalysis laboratoryUpload();
    void deleteLaboratory(String an, String sample);
    Observation callLaboratoryTests();
    CurrentD2DConnection getCurrentD2DConnection();
    ObservationLaboratoryCommandAnalysis observationCommand();
}
