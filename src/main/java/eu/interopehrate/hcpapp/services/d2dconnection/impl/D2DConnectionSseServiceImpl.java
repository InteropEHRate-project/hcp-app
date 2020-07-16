package eu.interopehrate.hcpapp.services.d2dconnection.impl;

import eu.interopehrate.hcpapp.mvc.commands.d2dconnection.D2DConnectionSseCommand;
import eu.interopehrate.hcpapp.services.d2dconnection.D2DConnectionSseService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class D2DConnectionSseServiceImpl implements D2DConnectionSseService {
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Override
    public SseEmitter buildSseEmitter() {
        SseEmitter emitter = new SseEmitter(5 * 60 * 1000L);
        emitter.onError(throwable -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));
        this.emitters.add(emitter);

        return emitter;
    }

    @EventListener
    public void onD2DConnectionEvent(D2DConnectionSseCommand d2DConnectionSseCommand) {
        this.emitters.forEach(emitter -> this.sendEvent(emitter, d2DConnectionSseCommand));
    }

    private void sendEvent(SseEmitter sseEmitter, D2DConnectionSseCommand d2DConnectionSseCommand) {
        try {
            sseEmitter.send(d2DConnectionSseCommand);
        } catch (Exception e) {
            sseEmitter.completeWithError(e);
        }
    }
}
