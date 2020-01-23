package eu.interopehrate.hcpapp.services.index.impl;

import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import eu.interopehrate.hcpapp.mvc.commands.IndexPatientDataCommand;
import eu.interopehrate.hcpapp.services.d2dconnection.BluetoothConnectionService;
import eu.interopehrate.hcpapp.services.index.IndexService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class IndexServiceImpl implements IndexService {
    @Value("${bluetooth.connection.info.image.size}")
    private String bluetoothConnectionInfoImageSize;
    private BluetoothConnectionService bluetoothConnectionService;
    private CurrentD2DConnection currentD2DConnection;
    private CurrentPatient currentPatient;

    public IndexServiceImpl(BluetoothConnectionService bluetoothConnectionService,
                            CurrentD2DConnection currentD2DConnection,
                            CurrentPatient currentPatient) {
        this.bluetoothConnectionService = bluetoothConnectionService;
        this.currentD2DConnection = currentD2DConnection;
        this.currentPatient = currentPatient;
    }

    @Override
    public IndexCommand d2dConnectionState() throws Exception {
        IndexCommand indexCommand = new IndexCommand();
        indexCommand.setConnectionState(currentD2DConnection.connectionState());
        indexCommand.setBluetoothConnectionInfoImage(this.connectionInfoQRCodePng());
        indexCommand.setBluetoothConnectionInfoImageSize(bluetoothConnectionInfoImageSize);

        IndexPatientDataCommand patientDataCommand = new IndexPatientDataCommand();
        if (Objects.nonNull(currentPatient.getPatient()) && Objects.nonNull(currentPatient.getPatient().getName())) {
            patientDataCommand.setFirstName(currentPatient.getPatient()
                    .getName()
                    .stream()
                    .map(humanName -> String.join(" ", humanName.getGivenAsSingleString()))
                    .collect(Collectors.joining(","))
            );
            patientDataCommand.setLastName(currentPatient.getPatient()
                    .getName()
                    .stream()
                    .map(humanName -> String.join(" ", humanName.getFamily()))
                    .collect(Collectors.joining(","))
            );
            patientDataCommand.setId(currentPatient.getPatient().getId());

        }
        if (Objects.nonNull(currentPatient.getConsent())) {
            patientDataCommand.setConsent(currentPatient.getConsentAsString());
        }
        indexCommand.setPatientDataCommand(patientDataCommand);
        return indexCommand;
    }

    @Override
    public void openConnection() {
        currentD2DConnection.open();
    }

    @Override
    public void closeConnection() {
        currentPatient.reset();
        currentD2DConnection.close();
    }

    private String connectionInfoQRCodePng() throws Exception {
        byte[] connectionInfoPng = bluetoothConnectionService.connectionInfoQRCodePng();
        String base64ConnectionInfoPng = Base64.getEncoder().encodeToString(connectionInfoPng);
        return String.join(",", "data:image/png;base64", base64ConnectionInfoPng);
    }
}
