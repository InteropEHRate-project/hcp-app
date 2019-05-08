package ro.siveco.europeanprojects.iehr.hcpwebapp.mvc.requestprocessors;

import org.springframework.stereotype.Component;
import ro.siveco.europeanprojects.iehr.hcpwebapp.services.BluetoothConnectionService;

import java.util.Base64;

@Component
public class BluetoothConnectionInfoBuilder {
    private BluetoothConnectionService bluetoothConnectionService;

    public BluetoothConnectionInfoBuilder(BluetoothConnectionService bluetoothConnectionService) {
        this.bluetoothConnectionService = bluetoothConnectionService;
    }

    public String connectionInfoQRCodePng(){
        byte[] connectionInfoPng = bluetoothConnectionService.connectionInfoQRCodePng();
        String base64ConnectionInfoPng = Base64.getEncoder().encodeToString(connectionInfoPng);
        return String.join(",", "data:image/png;base64", base64ConnectionInfoPng);
    }
}
