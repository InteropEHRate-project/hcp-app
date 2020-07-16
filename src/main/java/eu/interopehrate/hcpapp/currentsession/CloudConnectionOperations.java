package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.hcpapp.mvc.commands.emergency.CloudConnectionSseCommand;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CloudConnectionOperations {
    private final ApplicationEventPublisher eventPublisher;

    public CloudConnectionOperations(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @SneakyThrows
    public void reloadIndexPage() {
        Thread.sleep(150);
        CloudConnectionSseCommand cloudConnectionSseCommand = new CloudConnectionSseCommand(CloudConnectionSseCommand.SseCommandAction.RELOAD_PAGE, "");
        this.eventPublisher.publishEvent(cloudConnectionSseCommand);
    }
}

