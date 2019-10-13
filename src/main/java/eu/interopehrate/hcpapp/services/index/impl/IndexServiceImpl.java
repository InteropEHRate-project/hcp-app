package eu.interopehrate.hcpapp.services.index.impl;

import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import eu.interopehrate.hcpapp.services.d2dconnection.BluetoothConnectionService;
import eu.interopehrate.hcpapp.services.index.IndexService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class IndexServiceImpl implements IndexService {
    @Value("${bluetooth.connection.info.image.size}")
    private String bluetoothConnectionInfoImageSize;
    private BluetoothConnectionService bluetoothConnectionService;

    public IndexServiceImpl(BluetoothConnectionService bluetoothConnectionService) {
        this.bluetoothConnectionService = bluetoothConnectionService;
    }

    @Override
    public IndexCommand d2dConnectionState() throws Exception {
        IndexCommand indexCommand = new IndexCommand();
        indexCommand.setBluetoothConnectionInfoImage(this.connectionInfoQRCodePng());
        indexCommand.setBluetoothConnectionInfoImageSize(bluetoothConnectionInfoImageSize);
        return indexCommand;
    }

    private String connectionInfoQRCodePng() throws Exception {
        byte[] connectionInfoPng = bluetoothConnectionService.connectionInfoQRCodePng();
        String base64ConnectionInfoPng = Base64.getEncoder().encodeToString(connectionInfoPng);
        return String.join(",", "data:image/png;base64", base64ConnectionInfoPng);
    }
}
