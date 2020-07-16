package eu.interopehrate.hcpapp.mvc.controllers.cloudConnection;

import eu.interopehrate.hcpapp.mvc.commands.emergency.CloudConnectionSseCommand;
import eu.interopehrate.hcpapp.services.cloudConnection.CloudConnectionSseService;
import eu.interopehrate.hcpapp.services.d2dconnection.D2DConnectionSseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class CloudConnectionSseController {
    private final CloudConnectionSseService cloudConnectionSseService;

    public CloudConnectionSseController(CloudConnectionSseService cloudConnectionSseService) {
        this.cloudConnectionSseService = cloudConnectionSseService;
    }

    @GetMapping(path = "/cloud/connection/events/stream")
    public SseEmitter publishEvent() {
        return cloudConnectionSseService.buildSseEmitter();
    }
}
