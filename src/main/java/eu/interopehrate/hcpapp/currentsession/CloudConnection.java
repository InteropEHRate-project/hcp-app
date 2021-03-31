package eu.interopehrate.hcpapp.currentsession;

import ca.uhn.fhir.context.FhirContext;
import eu.interopehrate.hcpapp.mvc.commands.IndexPatientDataCommand;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.ihs.terminalclient.services.EmergencyService;
import eu.interopehrate.protocols.common.DocumentCategory;
import eu.interopehrate.r2demergency.R2DEmergencyImpl;
import eu.interopehrate.r2demergency.api.R2DEmergencyI;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CloudConnection implements DisposableBean {
    private CloudConnectionState connectionState = CloudConnectionState.OFF;
    private final CurrentPatient currentPatient;
    private final IndexPatientDataCommand indexPatientDataCommand;
    private final EmergencyService emergencyService;
    private final R2DEmergencyI r2dEmergency = new R2DEmergencyImpl();
    private final AuditInformationService auditInformationService;
    private String patientSummary;
    private String laboratoryResults;
    private String emergencyToken;

    public CloudConnection(CurrentPatient currentPatient,
                           IndexPatientDataCommand indexPatientDataCommand,
                           EmergencyService emergencyService, AuditInformationService auditInformationService) {
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

    public Boolean downloadIps(String url) {
        return this.getIps(url);
    }

    public Boolean downloadLaboratoryResults() {
        return this.getLaboratoryResults();
    }

    public void requestAccess(String qrCodeContent, String hospitalID) throws Exception {
        this.emergencyToken = r2dEmergency.requestAccess(qrCodeContent, hospitalID);
    }

    public void close() {
        this.closeConnection();
    }

    public CloudConnectionState connectionState() {
        return this.connectionState;
    }

    private Boolean getIps(String url) {
        try {
            this.patientSummary = this.r2dEmergency.get(this.emergencyToken, DocumentCategory.PATIENT_SUMMARY);
            Bundle patientSummary = (Bundle) FhirContext.forR4().newJsonParser().parseResource(this.patientSummary);
            this.currentPatient.initPatientSummary(patientSummary);
            return Boolean.TRUE;
        } catch (Exception e) {
            this.closeConnection();
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    private Boolean getLaboratoryResults() {
        try {
            this.laboratoryResults = this.r2dEmergency.get(emergencyToken, DocumentCategory.LABORATORY_REPORT);
            Bundle laboratoryResults = (Bundle) FhirContext.forR4().newJsonParser().parseResource(this.laboratoryResults);
            this.currentPatient.initLaboratoryResults(laboratoryResults);
            return Boolean.TRUE;
        } catch (Exception e) {
            this.closeConnection();
            e.printStackTrace();
            return Boolean.FALSE;
        }
    }

    private void closeConnection() {
        this.connectionState = CloudConnectionState.OFF;
        this.indexPatientDataCommand.setIpsReceived(false);
    }

}
