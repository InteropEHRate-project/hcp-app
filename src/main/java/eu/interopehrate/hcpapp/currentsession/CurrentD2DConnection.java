package eu.interopehrate.hcpapp.currentsession;

import eu.interopehrate.td2de.BluetoothConnection;
import eu.interopehrate.td2de.ConnectedThread;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CurrentD2DConnection implements DisposableBean {
    private BluetoothConnection bluetoothConnection;
    private ConnectedThread connectedThread;
    private Boolean on = Boolean.FALSE;

    @Override
    public void destroy() throws Exception {
        if (this.isActive()) {
            this.close();
        }
    }

    public void open() throws Exception {
        bluetoothConnection = new BluetoothConnection();
        connectedThread = bluetoothConnection.startListening();
        this.on = Boolean.TRUE;
    }

    public void close() throws Exception {
        this.bluetoothConnection.closeConnection();
        this.bluetoothConnection = null;
        this.connectedThread = null;
        this.on = Boolean.FALSE;
    }

    public Boolean isActive() {
        return this.on;
    }

    public void sendPractitioner(Practitioner practitioner) throws IOException {
        if (this.isActive()) {
            connectedThread.sendData(practitioner);
        } else {
            throw new RuntimeException("No D2D active connection.");
        }
    }

    public String lastPatientSummary() {
        if (this.isActive()) {
            return connectedThread.getLastSentPatientSummary();
        } else {
            throw new RuntimeException("No D2D active connection.");
        }
    }

    public Patient lastPatient() {
        if (this.isActive()) {
            return connectedThread.getLastSentData();
        } else {
            throw new RuntimeException("No D2D active connection.");
        }
    }
}
