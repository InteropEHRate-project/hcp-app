package eu.interopehrate.hcpapp.services.index.impl;

import eu.interopehrate.hcpapp.currentsession.CloudConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentD2DConnection;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.currentsession.D2DConnectionState;
import eu.interopehrate.hcpapp.mvc.commands.IndexCommand;
import eu.interopehrate.hcpapp.mvc.commands.IndexPatientDataCommand;
import eu.interopehrate.hcpapp.services.d2dconnection.BluetoothConnectionService;
import eu.interopehrate.hcpapp.services.index.IndexService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class IndexServiceImpl implements IndexService {
    @Value("${bluetooth.connection.info.image.size}")
    private String bluetoothConnectionInfoImageSize;
    private BluetoothConnectionService bluetoothConnectionService;

    private CurrentD2DConnection currentD2DConnection;
    private CloudConnection cloudConnection;
    private CurrentPatient currentPatient;

    public IndexServiceImpl(BluetoothConnectionService bluetoothConnectionService,
                            CurrentD2DConnection currentD2DConnection,
                            CloudConnection cloudConnection,
                            CurrentPatient currentPatient) {
        this.bluetoothConnectionService = bluetoothConnectionService;
        this.currentD2DConnection = currentD2DConnection;
        this.cloudConnection = cloudConnection;
        this.currentPatient = currentPatient;
    }

    public CurrentPatient getCurrentPatient() {
        return currentPatient;
    }

    @Override
    public IndexCommand indexCommand() throws Exception {
        IndexCommand indexCommand = new IndexCommand();
        indexCommand.setConnectionState(currentD2DConnection.connectionState());
        indexCommand.setCloudConnectionState(cloudConnection.connectionState());
        if (D2DConnectionState.PENDING_DEVICE.equals(currentD2DConnection.connectionState())) {
            indexCommand.setBluetoothConnectionInfoImage(this.connectionInfoQRCodePng());
            indexCommand.setBluetoothConnectionInfoImageSize(bluetoothConnectionInfoImageSize);
        }

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
            if (Objects.nonNull(currentPatient.getPatient().getGender())) {
                patientDataCommand.setGender(currentPatient.getPatient().getGender().toString());
            }
            if (Objects.nonNull(currentPatient.getPatient().getBirthDate())) {
                patientDataCommand.setBirthDate(currentPatient.getPatient().getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                patientDataCommand.setAge(Period.between(patientDataCommand.getBirthDate(), LocalDate.now()).getYears());
            }
            if (Objects.nonNull(this.currentPatient.getPatient().getAddress())
                    && this.currentPatient.getPatient().getAddress().size() > 0
                    && this.currentPatient.getPatient().getAddressFirstRep().hasState()) {
                patientDataCommand.setCountry(this.currentPatient.getPatient().getAddressFirstRep().getState());
            }
        }
        if (Objects.nonNull(currentPatient.getConsent())) {
            patientDataCommand.setConsent(currentPatient.getConsentAsString());
        }
        if (Objects.nonNull(currentPatient.getCertificate())) {
            patientDataCommand.setCertificate(currentPatient.getCertificateAsString());
        }

        if (currentD2DConnection.getIndexPatientDataCommand().getNoConformantJSON()) {
            patientDataCommand.setNoConformantJSON(true);
        }

        if (this.currentD2DConnection.getIndexPatientDataCommand().getIpsReceived() &&
                this.currentD2DConnection.getIndexPatientDataCommand().getPrescriptionReceived() &&
                this.currentD2DConnection.getIndexPatientDataCommand().getLaboratoryResultsReceived() &&
                this.currentD2DConnection.getIndexPatientDataCommand().getImageReportReceived() &&
                this.currentD2DConnection.getIndexPatientDataCommand().getPatHisReceived() &&
                this.currentD2DConnection.getIndexPatientDataCommand().getVitalSignsReceived()) {
            patientDataCommand.setIpsReceived(true);
            patientDataCommand.setPrescriptionReceived(true);
            patientDataCommand.setLaboratoryResultsReceived(true);
            patientDataCommand.setImageReportReceived(true);
            patientDataCommand.setPatHisReceived(true);
            patientDataCommand.setVitalSignsReceived(true);
        }

        patientDataCommand.setCertificate(currentD2DConnection.getIndexPatientDataCommand().getCertificate());
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

    @Override
    public void openCloudConnection() {
        cloudConnection.open();
    }

    @Override
    public void discardCloudConnection() {
        cloudConnection.discard();
    }

    @Override
    public void downloadCloudIps(String qrCode) {
        System.out.println("QR Code: " + qrCode);
        cloudConnection.downloadIps(qrCode);
    }

    @Override
    public void closeCloudConnection() {
        currentPatient.reset();
        cloudConnection.close();
    }

    @Override
    public void certificate() throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException {
        this.currentD2DConnection.certificate();
    }

    private String connectionInfoQRCodePng() throws Exception {
        byte[] connectionInfoPng = bluetoothConnectionService.connectionInfoQRCodePng();
        String base64ConnectionInfoPng = Base64.getEncoder().encodeToString(connectionInfoPng);
        return String.join(",", "data:image/png;base64", base64ConnectionInfoPng);
    }
}
