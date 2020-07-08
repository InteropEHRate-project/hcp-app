package eu.interopehrate.hcpapp.currentsession;
import eu.interopehrate.hcpapp.mvc.commands.emergency.EmergencyIndexPatientDataCommand;
import eu.interopehrate.td2de.ConnectedThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class CloudConnection implements DisposableBean {
    private final CurrentPatient currentPatient;
    private final CloudConnectionOperations cloudConnectionOperations;
    private CloudConnectionState connectionState = CloudConnectionState.OFF;
    private final EmergencyIndexPatientDataCommand emergencyIndexPatientDataCommand;

    public EmergencyIndexPatientDataCommand getEmergencyIndexPatientDataCommand() {
        return emergencyIndexPatientDataCommand;
    }

    public CloudConnection(CurrentPatient currentPatient, CloudConnectionOperations cloudConnectionOperations, EmergencyIndexPatientDataCommand emergencyIndexPatientDataCommand) {
        this.currentPatient = currentPatient;
        this.cloudConnectionOperations = cloudConnectionOperations;
        this.emergencyIndexPatientDataCommand = emergencyIndexPatientDataCommand;
    }

    @Override
    public void destroy() {
        this.closeConnection();
    }

    public void open() {
        this.connectionState = CloudConnectionState.PENDING;
        CompletableFuture.runAsync(this::openConnection)
                .thenRun(this::afterConnectionOpened);
    }

    public void close() {
        this.closeConnection();
    }

    public CloudConnectionState connectionState() {
        return this.connectionState;
    }

    private void openConnection() {
        this.connectionState = CloudConnectionState.ON;
        this.cloudConnectionOperations.reloadIndexPage();
    }

    private void afterConnectionOpened() {
    }

    private void closeConnection() {
        this.connectionState = CloudConnectionState.OFF;
        this.emergencyIndexPatientDataCommand.setIpsReceived(false);
    }

}
