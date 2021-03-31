package eu.interopehrate.hcpapp.currentsession;

import ca.uhn.fhir.context.FhirContext;
import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import eu.interopehrate.hcpapp.mvc.commands.IndexPatientDataCommand;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.protocols.common.DocumentCategory;
import eu.interopehrate.r2demergency.R2DEmergencyImpl;
import eu.interopehrate.r2demergency.api.R2DEmergencyI;
import lombok.SneakyThrows;
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
    private final R2DEmergencyI r2dEmergency = new R2DEmergencyImpl();
    private String emergencyToken;
    private final AuditInformationService auditInformationService;

    public CloudConnection(CurrentPatient currentPatient,
                           IndexPatientDataCommand indexPatientDataCommand, AuditInformationService auditInformationService) {
        this.currentPatient = currentPatient;
        this.indexPatientDataCommand = indexPatientDataCommand;
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

    public void requestAccess(String qrCodeContent, String hospitalID) throws Exception {
        this.emergencyToken = r2dEmergency.requestAccess(qrCodeContent, hospitalID);
    }

    public void close() {
        this.closeConnection();
    }

    public CloudConnectionState connectionState() {
        return this.connectionState;
    }

    private void closeConnection() {
        this.connectionState = CloudConnectionState.OFF;
        this.indexPatientDataCommand.setIpsReceived(false);
    }

    @SneakyThrows
    public Boolean download(String qrCodeContent, String hospitalId) {
        try {
            this.emergencyToken = this.r2dEmergency.requestAccess(qrCodeContent, hospitalId);
            String patientSummary = this.r2dEmergency.get(this.emergencyToken, DocumentCategory.PATIENT_SUMMARY);
            Bundle patientSummaryBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(patientSummary);
            List<Patient> patientList = patientSummaryBundle.getEntry()
                    .stream()
                    .map(Bundle.BundleEntryComponent::getResource)
                    .filter(resource -> resource.getResourceType().equals(ResourceType.Patient))
                    .map(Patient.class::cast)
                    .collect(Collectors.toList());
            if (patientList.size() > 0) {
                this.currentPatient.initPatient(patientList.get(0));
                this.currentPatient.initPatientSummary(patientSummaryBundle);
                this.connectionState = CloudConnectionState.ON;
                this.indexPatientDataCommand.setIpsReceived(true);
                this.auditInformationService.auditEmergencyGetIps();
            } else {
                this.closeConnection();
                return Boolean.TRUE;
            }
//            this.emergencyToken = this.r2dEmergency.requestAccess(qrCodeContent, hospitalId);
            String laboratoryResults = this.r2dEmergency.get(this.emergencyToken, DocumentCategory.LABORATORY_REPORT);
            Bundle laboratoryResultsBundle = (Bundle) FhirContext.forR4().newJsonParser().parseResource(laboratoryResults);
            this.currentPatient.initLaboratoryResults(laboratoryResultsBundle);

            IndexCommand.transmissionCompleted = Boolean.TRUE;
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error(e.getMessage());
            this.closeConnection();
            return Boolean.FALSE;
        }
    }
}
