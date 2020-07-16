package eu.interopehrate.hcpapp.services.cloudConnection;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface CloudConnectionSseService {
    SseEmitter buildSseEmitter();
}
