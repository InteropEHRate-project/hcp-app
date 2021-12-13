package eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.InstrumentsExamType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Entity(name = "INSTRUMENTS_EXAMS")
public class InstrumentsExaminationEntity extends HCPApplicationEntity {
    @Enumerated(EnumType.STRING)
    private InstrumentsExamType type;
    private String result;
}
