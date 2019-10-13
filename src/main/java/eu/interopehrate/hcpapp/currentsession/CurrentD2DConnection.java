package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.td2de.BluetoothConnection;
import eu.interopehrate.td2de.ConnectedThread;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public class CurrentD2DConnection implements DisposableBean {
    private BluetoothConnection bluetoothConnection;
    private ConnectedThread connectedThread;
    private D2DConnectionState connectionState = D2DConnectionState.OFF;

    @Override
    public void destroy() {
        if (Objects.nonNull(bluetoothConnection)) {
            this.closeConnection();
        }
    }

    public void open() {
        this.connectionState = D2DConnectionState.PENDING_DEVICE;
        CompletableFuture.runAsync(this::openConnection);
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
        } catch (IOException e) {
            this.connectionState = D2DConnectionState.OFF;
            throw new RuntimeException(e);
        }
    }

    private void closeConnection() {
        try {
            //todo  - add an issue to uprc for introducing a metohod for aborting the connection
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
}
