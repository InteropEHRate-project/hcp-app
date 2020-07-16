package eu.interopehrate.hcpapp.currentsession;
import eu.interopehrate.hcpapp.mvc.commands.IndexPatientDataCommand;
import eu.interopehrate.ihs.terminalclient.services.EmergencyService;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.ResourceType;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CloudConnection implements DisposableBean {
    private final CurrentPatient currentPatient;
    private final CloudConnectionOperations cloudConnectionOperations;
    private IndexPatientDataCommand indexPatientDataCommand;
    private CloudConnectionState connectionState = CloudConnectionState.OFF;
    private EmergencyService emergencyService;

    public CloudConnection(CurrentPatient currentPatient,
                           CloudConnectionOperations cloudConnectionOperations,
                           IndexPatientDataCommand indexPatientDataCommand,
                           EmergencyService emergencyService) {
        this.currentPatient = currentPatient;
        this.cloudConnectionOperations = cloudConnectionOperations;
        this.indexPatientDataCommand = indexPatientDataCommand;
        this.emergencyService = emergencyService;
    }

    @Override
    public void destroy() {
        this.closeConnection();
    }

    public void open() {
        this.connectionState = CloudConnectionState.PENDING;
        this.cloudConnectionOperations.reloadIndexPage();
    }

    public void discard() {
        this.connectionState = CloudConnectionState.OFF;
        this.cloudConnectionOperations.reloadIndexPage();
    }

    public void downloadIps(String url) {
        this.openConnection(url);
    }

    public void close() {
        this.closeConnection();
    }

    public CloudConnectionState connectionState() {
        return this.connectionState;
    }

    private void openConnection(String url) {
        try {
            Bundle cloudIps = this.emergencyService.getIps(url);

            List<Patient> patientList = cloudIps.getEntry()
                    .stream()
                    .filter(bec -> bec.getResource().getResourceType().equals(ResourceType.Patient))
                    .map(Bundle.BundleEntryComponent::getResource)
                    .map(Patient.class::cast)
                    .collect(Collectors.toList());

            if (patientList.size() > 0) {
                this.currentPatient.initPatient(patientList.get(0));
                this.currentPatient.initPatientSummary(cloudIps);
                this.connectionState = CloudConnectionState.ON;
                this.indexPatientDataCommand.setIpsReceived(true);
                this.cloudConnectionOperations.reloadIndexPage();
            } else {
                this.closeConnection();
                this.cloudConnectionOperations.reloadIndexPage();
            }

        } catch (Exception e) {
            this.closeConnection();
            this.cloudConnectionOperations.reloadIndexPage();
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        this.connectionState = CloudConnectionState.OFF;
        this.indexPatientDataCommand.setIpsReceived(false);
    }

}
