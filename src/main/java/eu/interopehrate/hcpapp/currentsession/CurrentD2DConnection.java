package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.hcpapp.mvc.commands.d2dconnection.D2DConnectionSseCommand;
import eu.interopehrate.hcpapp.services.ApplicationRuntimeInfoService;
import eu.interopehrate.hcpapp.services.administration.AdmissionDataAuditService;
import eu.interopehrate.td2de.BluetoothConnection;
import eu.interopehrate.td2de.ConnectedThread;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public class CurrentD2DConnection implements DisposableBean {
    private final ApplicationEventPublisher eventPublisher;
    private final ApplicationRuntimeInfoService applicationRuntimeInfoService;
    private BluetoothConnection bluetoothConnection;
    private ConnectedThread connectedThread;
    private D2DConnectionState connectionState = D2DConnectionState.OFF;
    private AdmissionDataAuditService admissionDataAuditService;

    public CurrentD2DConnection(ApplicationEventPublisher eventPublisher,
                                ApplicationRuntimeInfoService applicationRuntimeInfoService,
                                AdmissionDataAuditService admissionDataAuditService) {
        this.eventPublisher = eventPublisher;
        this.applicationRuntimeInfoService = applicationRuntimeInfoService;
        this.admissionDataAuditService = admissionDataAuditService;
    }

    @Override
    public void destroy() {
        if (Objects.nonNull(bluetoothConnection)) {
            this.closeConnection();
        }
    }

    public void open() {
        this.connectionState = D2DConnectionState.PENDING_DEVICE;
        CompletableFuture.runAsync(this::openConnection)
                .thenRun(this::afterOpenConnection);
    }

    public void close() {
        CompletableFuture.runAsync(this::closeConnection);
    }

    public D2DConnectionState connectionState() {
        return this.connectionState;
    }

    public void sendPractitioner(Practitioner practitioner) throws IOException {
        if (D2DConnectionState.ON.equals(connectionState)) {
            connectedThread.sendData(practitioner);
        } else {
            throw new RuntimeException("No D2D active connection.");
        }
    }

    public String lastPatientSummary() {
        if (D2DConnectionState.ON.equals(connectionState)) {
            return connectedThread.getLastSentPatientSummary();
        } else {
            throw new RuntimeException("No D2D active connection.");
        }
    }

    public Patient lastPatient() {
        if (D2DConnectionState.ON.equals(connectionState)) {
            return connectedThread.getLastSentData();
        } else {
            throw new RuntimeException("No D2D active connection.");
        }
    }

    private void openConnection() {
        try {
            bluetoothConnection = new BluetoothConnection();
            connectedThread = bluetoothConnection.startListening();
            this.connectionState = D2DConnectionState.ON;
            this.publishBTConnectionEstablished();
        } catch (IOException e) {
            this.connectionState = D2DConnectionState.OFF;
            throw new RuntimeException(e);
        }
    }

    private void closeConnection() {
        try {
            if (Objects.nonNull(connectedThread)) {
                this.bluetoothConnection.closeConnection();
            }
            this.bluetoothConnection = null;
            this.connectedThread = null;
            this.connectionState = D2DConnectionState.OFF;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void publishBTConnectionEstablished() {
        D2DConnectionSseCommand d2DConnectionSseCommand = new D2DConnectionSseCommand(D2DConnectionSseCommand.SseCommandAction.RELOAD_PAGE, "");
        this.eventPublisher.publishEvent(d2DConnectionSseCommand);
    }

    private void afterOpenConnection() {
        try {
            this.sendPractitioner(applicationRuntimeInfoService.practitioner());
            admissionDataAuditService.saveAdmissionData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}