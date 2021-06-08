package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.hcpapp.mvc.commands.d2dconnection.D2DConnectionSseCommand;
import eu.interopehrate.hcpapp.services.ApplicationRuntimeInfoService;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.td2de.api.TD2D;
import eu.interopehrate.td2de.api.TD2DSecureConnectionFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class D2DConnectionOperations {
    private final ApplicationEventPublisher eventPublisher;
    private final ApplicationRuntimeInfoService applicationRuntimeInfoService;
    private final AuditInformationService auditInformationService;

    public D2DConnectionOperations(ApplicationEventPublisher eventPublisher,
                                   ApplicationRuntimeInfoService applicationRuntimeInfoService,
                                   AuditInformationService auditInformationService) {
        this.eventPublisher = eventPublisher;
        this.applicationRuntimeInfoService = applicationRuntimeInfoService;
        this.auditInformationService = auditInformationService;
    }

    @SneakyThrows
    public void reloadIndexPage() {
        Thread.sleep(150);
        D2DConnectionSseCommand d2DConnectionSseCommand = new D2DConnectionSseCommand(D2DConnectionSseCommand.SseCommandAction.RELOAD_PAGE, "");
        this.eventPublisher.publishEvent(d2DConnectionSseCommand);
    }

    @SneakyThrows
    public TD2D sendPractitionerIdentity(TD2DSecureConnectionFactory secureConnectionFactory) throws IOException {
        return secureConnectionFactory.createSecureConnection(applicationRuntimeInfoService.practitioner());
    }

    public void auditPatientAdmission() {
        this.auditInformationService.auditAdmissionData();
    }

    public void auditPatientConsent() {
        this.auditInformationService.auditConsentData();
    }
}
