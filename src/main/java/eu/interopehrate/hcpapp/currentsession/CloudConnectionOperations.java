package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.hcpapp.mvc.commands.emergency.CloudConnectionSseCommand;
import eu.interopehrate.hcpapp.services.ApplicationRuntimeInfoService;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CloudConnectionOperations {
    private final ApplicationEventPublisher eventPublisher;
    private final ApplicationRuntimeInfoService applicationRuntimeInfoService;
    private final AuditInformationService auditInformationService;

    public CloudConnectionOperations(ApplicationEventPublisher eventPublisher,
                                   ApplicationRuntimeInfoService applicationRuntimeInfoService,
                                   AuditInformationService auditInformationService) {
        this.eventPublisher = eventPublisher;
        this.applicationRuntimeInfoService = applicationRuntimeInfoService;
        this.auditInformationService = auditInformationService;
    }

    @SneakyThrows
    public void reloadIndexPage() {
        Thread.sleep(150);
        CloudConnectionSseCommand cloudConnectionSseCommand = new CloudConnectionSseCommand(CloudConnectionSseCommand.SseCommandAction.RELOAD_PAGE, "");
        this.eventPublisher.publishEvent(cloudConnectionSseCommand);
    }
}

