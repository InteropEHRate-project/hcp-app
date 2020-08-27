package eu.interopehrate.hcpapp.services.currentpatient.impl.currentmedications;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.currentmedications.PrescriptionInfoCommand;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

class PrescriptionServiceImplTest {

    @Test
    void testPaginationCreation() {
        int pageNo = 1;
        int pageSize = 3;
        List<PrescriptionInfoCommand> prescriptions = new ArrayList<>();
        prescriptions.add(new PrescriptionInfoCommand());
        prescriptions.get(0).setDrugName("A");
        prescriptions.get(0).setStatus("Active");
        prescriptions.add(new PrescriptionInfoCommand());
        prescriptions.get(1).setDrugName("B");
        prescriptions.get(1).setStatus("Stopped");
        prescriptions.add(new PrescriptionInfoCommand());
        prescriptions.get(2).setDrugName("b");
        prescriptions.get(2).setStatus("Active");
        prescriptions.add(new PrescriptionInfoCommand());
        prescriptions.get(3).setDrugName("D");
        prescriptions.get(3).setStatus("Active");
        prescriptions.add(new PrescriptionInfoCommand());
        prescriptions.get(4).setDrugName("E");
        prescriptions.get(4).setStatus("On-hold");
        prescriptions.add(new PrescriptionInfoCommand());
        prescriptions.get(5).setDrugName("F");
        prescriptions.get(5).setStatus("Active");
        prescriptions.add(new PrescriptionInfoCommand());
        prescriptions.get(6).setDrugName("G");
        prescriptions.get(6).setStatus("Stopped");
        prescriptions.add(new PrescriptionInfoCommand());
        prescriptions.get(7).setDrugName("H");
        prescriptions.get(7).setStatus("Active");

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        int index = (pageNo - 1) * pageSize;
        Page<PrescriptionInfoCommand> page = new PageImpl<>(prescriptions.subList(index, index + pageSize), pageable, prescriptions.size());

        Assert.assertEquals(pageSize, page.getContent().size());
        Assert.assertEquals(3, page.getTotalPages());
        Assert.assertEquals(prescriptions.size(), page.getTotalElements());
    }
}