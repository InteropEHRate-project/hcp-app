package eu.interopehrate.hcpapp.mvc.commands;

import eu.interopehrate.hcpapp.currentsession.CloudConnectionState;
import eu.interopehrate.hcpapp.currentsession.D2DConnectionState;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class IndexCommand {
    private String bluetoothConnectionInfoImage;
    private String bluetoothConnectionInfoImageSize;
    private D2DConnectionState connectionState;
    private CloudConnectionState cloudConnectionState;
    private IndexPatientDataCommand patientDataCommand;
    public static Boolean transmissionCompleted = false;

    @NotEmpty
    @NotNull
    private String qrCode;
    private String hospitalID = "hospital";
}
