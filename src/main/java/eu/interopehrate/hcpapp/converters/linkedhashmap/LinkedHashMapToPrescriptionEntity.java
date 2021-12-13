package eu.interopehrate.hcpapp.converters.linkedhashmap;

import eu.interopehrate.hcpapp.jpa.entities.currentpatient.PrescriptionEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Objects;

@Component
public class LinkedHashMapToPrescriptionEntity implements Converter<LinkedHashMap, PrescriptionEntity> {
    @Override
    public PrescriptionEntity convert(LinkedHashMap source) {
        PrescriptionEntity prescriptionEntity = new PrescriptionEntity();
        if (Objects.nonNull(source.get("patient"))) {
            prescriptionEntity.setPatientId(((LinkedHashMap) source.get("patient")).get("patientId").toString());
        }
        prescriptionEntity.setStatus(source.get("status").toString());
        prescriptionEntity.setFrequency((Integer) source.get("frequency"));
        prescriptionEntity.setPeriod((Integer) source.get("period"));
        prescriptionEntity.setPeriodUnit(source.get("periodUnit").toString());
        if (Objects.nonNull(source.get("timings"))) {
            prescriptionEntity.setTimings(source.get("timings").toString());
        }
        if (Objects.nonNull(source.get("start"))) {
            prescriptionEntity.setStart(LocalDate.parse(source.get("start").toString()));
        }
        if (Objects.nonNull(source.get("end"))) {
            prescriptionEntity.setEnd(LocalDate.parse(source.get("end").toString()));
        }
        prescriptionEntity.setDrugName(source.get("drugName").toString());
        prescriptionEntity.setDrugDosage(source.get("drugDosage").toString());
        prescriptionEntity.setNotes(source.get("notes").toString());
        if (Objects.nonNull(source.get("author"))) {
            prescriptionEntity.setAuthor(source.get("author").toString());
        }
        prescriptionEntity.setDateOfPrescription(LocalDate.parse(source.get("dateOfPrescription").toString()));
        return prescriptionEntity;
    }
}
