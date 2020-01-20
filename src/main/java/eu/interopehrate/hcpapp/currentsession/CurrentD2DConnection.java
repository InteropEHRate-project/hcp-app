package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.hcpapp.mvc.commands.d2dconnection.D2DConnectionSseCommand;
import eu.interopehrate.hcpapp.services.ApplicationRuntimeInfoService;
import eu.interopehrate.hcpapp.services.administration.AdmissionDataAuditService;
import eu.interopehrate.td2de.BluetoothConnection;
import eu.interopehrate.td2de.ConnectedThread;
import eu.interopehrate.td2de.api.D2DConnectionListeners;
import eu.interopehrate.td2de.api.D2DHRExchangeListeners;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class CurrentD2DConnection implements DisposableBean {
    private final CurrentPatient currentPatient;
    private final ApplicationEventPublisher eventPublisher;
    private final ApplicationRuntimeInfoService applicationRuntimeInfoService;
    private final AdmissionDataAuditService admissionDataAuditService;
    private BluetoothConnection bluetoothConnection;
    private ConnectedThread connectedThread;
    private D2DConnectionState connectionState = D2DConnectionState.OFF;

    public CurrentD2DConnection(ApplicationEventPublisher eventPublisher,
                                ApplicationRuntimeInfoService applicationRuntimeInfoService,
                                AdmissionDataAuditService admissionDataAuditService,
                                CurrentPatient currentPatient) {
        this.currentPatient = currentPatient;
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
        this.closeConnection();
    }

    public D2DConnectionState connectionState() {
        return this.connectionState;
    }

    @Deprecated
    public void sendPractitioner(Practitioner practitioner) throws IOException {
        throw new RuntimeException("will be removed");
    }

    @Deprecated
    public String lastPatientSummary() {
        throw new RuntimeException("will be removed");
    }

    @Deprecated
    public Patient lastPatient() {
        throw new RuntimeException("will be removed");
    }

    private void openConnection() {
        try {
            bluetoothConnection = new BluetoothConnection();
            connectedThread = bluetoothConnection.listenConnection(new D2DHRExchangeListener(), new D2DConnectionListener());
            this.connectionState = D2DConnectionState.ON;
            this.publishReloadPageEvent();
        } catch (IOException e) {
            this.closeConnection();
            throw new RuntimeException(e);
        }
    }

    private void closeConnection() {
        try {
            if (Objects.nonNull(connectedThread)) {
                this.bluetoothConnection.closeConnection();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            this.bluetoothConnection = null;
            this.connectedThread = null;
            this.connectionState = D2DConnectionState.OFF;
        }
    }

    private void publishReloadPageEvent() {
        D2DConnectionSseCommand d2DConnectionSseCommand = new D2DConnectionSseCommand(D2DConnectionSseCommand.SseCommandAction.RELOAD_PAGE, "");
        this.eventPublisher.publishEvent(d2DConnectionSseCommand);
    }

    private void afterOpenConnection() {
        try {
            this.connectedThread.sendPersonalIdentity(applicationRuntimeInfoService.practitioner());
        } catch (Exception e) {
            this.closeConnection();
            throw new RuntimeException(e);
        }
    }

    private class D2DConnectionListener implements D2DConnectionListeners {
        @Override
        public void onConnectionClosure() {
            log.info("D2D connection was closed.");
        }
    }

    private class D2DHRExchangeListener implements D2DHRExchangeListeners {
        @Override
        public void onPersonalIdentityReceived(Patient patient) {
            CurrentD2DConnection.this.currentPatient.initPatient(patient);
            CurrentD2DConnection.this.publishReloadPageEvent();
        }

        @Override
        public void onPatientSummaryReceived(Bundle bundle) {
            CurrentD2DConnection.this.currentPatient.initPatientSummary(bundle);
            CurrentD2DConnection.this.admissionDataAuditService.saveAdmissionData();
        }

        @Override
        public void onConsentAnswerReceived(String s) {
            System.out.println("onConsentAnswerReceived");
        }
    }
}