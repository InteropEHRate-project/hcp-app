package eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.phexam.PHExamType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Entity(name = "PH_EXAMS")
public class PHExamEntity extends HCPApplicationEntity {
    @Enumerated(EnumType.STRING)
    private PHExamType type;
    private String result;
}
