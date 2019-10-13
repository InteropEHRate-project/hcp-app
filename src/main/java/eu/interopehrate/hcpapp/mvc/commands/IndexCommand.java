package eu.interopehrate.hcpapp.mvc.commands;

import eu.interopehrate.hcpapp.currentsession.D2DConnectionState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndexCommand {
    private String bluetoothConnectionInfoImage;
    private String bluetoothConnectionInfoImageSize;
    private D2DConnectionState connectionState;
}
