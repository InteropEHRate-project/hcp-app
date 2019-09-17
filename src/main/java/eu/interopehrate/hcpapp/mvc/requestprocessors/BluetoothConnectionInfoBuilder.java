package eu.interopehrate.hcpapp.mvc.requestprocessors;

import eu.interopehrate.hcpapp.services.BluetoothConnectionService;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class BluetoothConnectionInfoBuilder {
    private BluetoothConnectionService bluetoothConnectionService;

    public BluetoothConnectionInfoBuilder(BluetoothConnectionService bluetoothConnectionService) {
        this.bluetoothConnectionService = bluetoothConnectionService;
    }

    public String connectionInfoQRCodePng() throws Exception {
        byte[] connectionInfoPng = bluetoothConnectionService.connectionInfoQRCodePng();
        String base64ConnectionInfoPng = Base64.getEncoder().encodeToString(connectionInfoPng);
        return String.join(",", "data:image/png;base64", base64ConnectionInfoPng);
    }
}
