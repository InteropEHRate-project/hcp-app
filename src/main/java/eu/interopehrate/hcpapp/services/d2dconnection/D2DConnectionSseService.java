package eu.interopehrate.hcpapp.services.d2dconnection;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface D2DConnectionSseService {
    SseEmitter buildSseEmitter();
}
