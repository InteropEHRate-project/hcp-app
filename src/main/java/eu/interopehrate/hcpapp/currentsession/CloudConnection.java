package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.hcpapp.mvc.commands.IndexPatientDataCommand;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
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
    private CloudConnectionState connectionState = CloudConnectionState.OFF;
    private final CurrentPatient currentPatient;
    private final IndexPatientDataCommand indexPatientDataCommand;
    private final EmergencyService emergencyService;
    private final AuditInformationService auditInformationService;

    public CloudConnection(CurrentPatient currentPatient,
                           IndexPatientDataCommand indexPatientDataCommand,
                           EmergencyService emergencyService,
                           AuditInformationService auditInformationService) {
        this.currentPatient = currentPatient;
        this.indexPatientDataCommand = indexPatientDataCommand;
        this.emergencyService = emergencyService;
        this.auditInformationService = auditInformationService;
    }

    @Override
    public void destroy() {
        this.closeConnection();
    }

    public void open() {
        this.connectionState = CloudConnectionState.PENDING;
    }

    public void discard() {
        this.connectionState = CloudConnectionState.OFF;
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
                this.auditInformationService.auditEmergencyGetIps();
            } else {
                this.closeConnection();
            }

        } catch (Exception e) {
            this.closeConnection();
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        this.connectionState = CloudConnectionState.OFF;
        this.indexPatientDataCommand.setIpsReceived(false);
    }

}
