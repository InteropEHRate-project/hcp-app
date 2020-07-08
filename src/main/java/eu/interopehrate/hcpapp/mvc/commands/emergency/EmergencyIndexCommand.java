package eu.interopehrate.hcpapp.mvc.commands.emergency;

import eu.interopehrate.hcpapp.currentsession.CloudConnectionState;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
public class EmergencyIndexCommand {
    private CloudConnectionState connectionState;
    private EmergencyIndexPatientDataCommand patientDataCommand;
    @NotEmpty
    @NotNull
    private String qrCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmergencyIndexCommand that = (EmergencyIndexCommand) o;
        return connectionState == that.connectionState &&
                Objects.equals(patientDataCommand, that.patientDataCommand) &&
                Objects.equals(qrCode, that.qrCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionState, patientDataCommand, qrCode);
    }
}
