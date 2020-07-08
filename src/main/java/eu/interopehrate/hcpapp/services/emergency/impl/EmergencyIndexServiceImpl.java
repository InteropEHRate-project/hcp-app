package eu.interopehrate.hcpapp.services.emergency.impl;

import eu.interopehrate.hcpapp.currentsession.*;
import eu.interopehrate.hcpapp.mvc.commands.emergency.EmergencyIndexCommand;
import eu.interopehrate.hcpapp.mvc.commands.emergency.EmergencyIndexPatientDataCommand;
import eu.interopehrate.hcpapp.services.emergency.EmergencyIndexService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EmergencyIndexServiceImpl implements EmergencyIndexService {

    private CloudConnection cloudConnection;
    private CurrentPatient currentPatient;

    public EmergencyIndexServiceImpl(CloudConnection cloudConnection,
                            CurrentPatient currentPatient) {
        this.cloudConnection = cloudConnection;
        this.currentPatient = currentPatient;
    }

    @Override
    public EmergencyIndexCommand emergencyIndexCommand() throws Exception {
        EmergencyIndexCommand indexCommand = new EmergencyIndexCommand();
        indexCommand.setConnectionState(cloudConnection.connectionState());

        EmergencyIndexPatientDataCommand patientDataCommand = new EmergencyIndexPatientDataCommand();
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
                patientDataCommand.setBirthDate(currentPatient.getPatient().getBirthDate());
            }
        }
        indexCommand.setPatientDataCommand(patientDataCommand);
        return indexCommand;
    }

    @Override
    public void openConnection(String qrCode) {
        System.out.println("QR Code: " + qrCode);
        cloudConnection.open();
    }

    @Override
    public void closeConnection() {
        currentPatient.reset();
        cloudConnection.close();
    }
}
