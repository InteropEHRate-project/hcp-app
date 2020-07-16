package eu.interopehrate.hcpapp.services.cloudConnection.impl;

import eu.interopehrate.hcpapp.mvc.commands.emergency.CloudConnectionSseCommand;
import eu.interopehrate.hcpapp.services.cloudConnection.CloudConnectionSseService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CloudConnectionSseServiceImpl implements CloudConnectionSseService {
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Override
    public SseEmitter buildSseEmitter() {
        SseEmitter emitter = new SseEmitter(50 * 60 * 1000L);
        emitter.onError(throwable -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));
        this.emitters.add(emitter);

        return emitter;
    }

    @EventListener
    public void onCloudConnectionEvent(CloudConnectionSseCommand cloudConnectionSseCommand) {
        this.emitters.forEach(emitter -> this.sendEvent(emitter, cloudConnectionSseCommand));
    }

    private void sendEvent(SseEmitter sseEmitter, CloudConnectionSseCommand cloudConnectionSseCommand) {
        try {
            sseEmitter.send(cloudConnectionSseCommand);
        } catch (Exception e) {
            sseEmitter.completeWithError(e);
        }
    }
}
