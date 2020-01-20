package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.hcpapp.mvc.commands.d2dconnection.D2DConnectionSseCommand;
import eu.interopehrate.hcpapp.services.ApplicationRuntimeInfoService;
import eu.interopehrate.hcpapp.services.administration.AdmissionDataAuditService;
import eu.interopehrate.td2de.ConnectedThread;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class D2DConnectionOperations {
    private final ApplicationEventPublisher eventPublisher;
    private final ApplicationRuntimeInfoService applicationRuntimeInfoService;
    private final AdmissionDataAuditService admissionDataAuditService;

    public D2DConnectionOperations(ApplicationEventPublisher eventPublisher,
                                   ApplicationRuntimeInfoService applicationRuntimeInfoService,
                                   AdmissionDataAuditService admissionDataAuditService) {
        this.eventPublisher = eventPublisher;
        this.applicationRuntimeInfoService = applicationRuntimeInfoService;
        this.admissionDataAuditService = admissionDataAuditService;
    }

    public void reloadIndexPage() {
        D2DConnectionSseCommand d2DConnectionSseCommand = new D2DConnectionSseCommand(D2DConnectionSseCommand.SseCommandAction.RELOAD_PAGE, "");
        this.eventPublisher.publishEvent(d2DConnectionSseCommand);
    }

    public void sendPractitionerIdentity(ConnectedThread connectedThread) throws IOException {
        connectedThread.sendPersonalIdentity(applicationRuntimeInfoService.practitioner());
    }

    public void auditPatientAdmission() {
        this.admissionDataAuditService.saveAdmissionData();
    }
}
