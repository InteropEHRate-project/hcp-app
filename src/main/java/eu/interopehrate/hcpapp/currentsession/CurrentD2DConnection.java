package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.td2de.BluetoothConnection;
import eu.interopehrate.td2de.ConnectedThread;
import eu.interopehrate.td2de.api.D2DConnectionListeners;
import eu.interopehrate.td2de.api.D2DHRExchangeListeners;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class CurrentD2DConnection implements DisposableBean {
    private final CurrentPatient currentPatient;
    private final D2DConnectionOperations d2DConnectionOperations;
    private BluetoothConnection bluetoothConnection;
    private ConnectedThread connectedThread;
    private D2DConnectionState connectionState = D2DConnectionState.OFF;

    public CurrentD2DConnection(CurrentPatient currentPatient,
                                D2DConnectionOperations d2DConnectionOperations) {
        this.currentPatient = currentPatient;
        this.d2DConnectionOperations = d2DConnectionOperations;
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

    private void openConnection() {
        try {
            bluetoothConnection = new BluetoothConnection();
            connectedThread = bluetoothConnection.listenConnection(new D2DHRExchangeListener(), new D2DConnectionListener());
            this.connectionState = D2DConnectionState.ON;
            this.d2DConnectionOperations.reloadIndexPage();
        } catch (IOException e) {
            this.closeConnection();
            throw new RuntimeException(e);
        }
    }

    private void afterOpenConnection() {
        try {
            this.d2DConnectionOperations.sendPractitionerIdentity(this.connectedThread);
        } catch (Exception e) {
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

    private class D2DConnectionListener implements D2DConnectionListeners {
        @Override
        public void onConnectionClosure() {
            log.info("D2D connection was closed.");
        }
    }

    private class D2DHRExchangeListener implements D2DHRExchangeListeners {
        @SneakyThrows
        @Override
        public void onPersonalIdentityReceived(Patient patient) {
            CurrentD2DConnection.this.currentPatient.initPatient(patient);
            Thread.sleep(150);
            CurrentD2DConnection.this.d2DConnectionOperations.reloadIndexPage();
        }

        @Override
        public void onPatientSummaryReceived(Bundle bundle) {
            CurrentD2DConnection.this.currentPatient.initPatientSummary(bundle);
            CurrentD2DConnection.this.d2DConnectionOperations.auditPatientAdmission();
        }

        @Override
        public void onConsentAnswerReceived(String s) {
            log.info(String.format("Consent received - %s", s));
        }
    }
}