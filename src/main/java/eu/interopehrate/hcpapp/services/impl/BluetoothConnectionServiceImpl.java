package eu.interopehrate.hcpapp.services.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import eu.interopehrate.hcpapp.services.BluetoothConnectionService;
import eu.interopehrate.td2de.BluetoothConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class BluetoothConnectionServiceImpl implements BluetoothConnectionService {
    @Value("${bluetooth.connection.info.image.size}")
    private Integer bluetoothConnectionInfoImageSize;

    @Override
    public byte[] connectionInfoQRCodePng() throws Exception {
        String connectionInfo = new BluetoothConnection().getBTadapterAddress();
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(connectionInfo, BarcodeFormat.QR_CODE, bluetoothConnectionInfoImageSize, bluetoothConnectionInfoImageSize);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
        return pngOutputStream.toByteArray();
    }
}
