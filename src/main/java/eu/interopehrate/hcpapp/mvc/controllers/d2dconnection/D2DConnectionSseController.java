package eu.interopehrate.hcpapp.mvc.controllers.d2dconnection;

import eu.interopehrate.hcpapp.services.d2dconnection.D2DConnectionSseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class D2DConnectionSseController {
    private final D2DConnectionSseService d2DConnectionSseService;

    public D2DConnectionSseController(D2DConnectionSseService d2DConnectionSseService) {
        this.d2DConnectionSseService = d2DConnectionSseService;
    }

    @GetMapping(path = "/d2d/connection/events/stream")
    public SseEmitter publishEvent() {
        return d2DConnectionSseService.buildSseEmitter();
    }
}
