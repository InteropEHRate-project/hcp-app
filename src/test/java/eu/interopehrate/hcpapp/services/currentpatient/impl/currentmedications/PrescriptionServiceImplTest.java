package eu.interopehrate.hcpapp.services.currentpatient.impl.currentmedications;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class PrescriptionServiceImplTest {

    @Test
    void testPaginationCreation() {
        List<PrescriptionInfoCommand> prescriptions = new ArrayList<>();
        prescriptions.add(new PrescriptionInfoCommand());
        prescriptions.get(0).setDrugName("A");
        prescriptions.add(new PrescriptionInfoCommand());
        prescriptions.get(1).setDrugName("B");
        prescriptions.add(new PrescriptionInfoCommand());
        prescriptions.get(2).setDrugName("C");
        prescriptions.add(new PrescriptionInfoCommand());
        prescriptions.get(3).setDrugName("D");
        prescriptions.add(new PrescriptionInfoCommand());
        prescriptions.get(4).setDrugName("E");

        List<List<PrescriptionInfoCommand>> listOfListsOfPrescriptions = new ArrayList<>();
        List<PrescriptionInfoCommand> listOfPrescriptions = new ArrayList<>();
        try {
            while (!prescriptions.isEmpty()) {
                int i = 0;
                do {
                    listOfPrescriptions.add(prescriptions.get(i));
                    i++;
                } while ( i < 3);   //change condition for "i" to change the page size.
                listOfListsOfPrescriptions.add(listOfPrescriptions);
                listOfPrescriptions = new ArrayList<>();
                for (int j = 0; j < i; j++) {
                    prescriptions.remove(0);
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
            listOfListsOfPrescriptions.add(listOfPrescriptions);
            prescriptions.clear();
        }
        for (int j = 0; j < listOfListsOfPrescriptions.size(); j++) {
            for (PrescriptionInfoCommand pr : listOfListsOfPrescriptions.get(j)) {
                System.out.println(pr.getDrugName());
            }
            System.out.println();
        }
    }
}