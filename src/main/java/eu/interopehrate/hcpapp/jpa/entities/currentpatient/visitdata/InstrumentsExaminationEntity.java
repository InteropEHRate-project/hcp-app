package eu.interopehrate.hcpapp.jpa.entities.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.InstrumentsExamType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "INSTRUMENTS_EXAMS")
public class InstrumentsExaminationEntity extends HCPApplicationEntity {
    @Enumerated(EnumType.STRING)
    private InstrumentsExamType type;
    @Column(name = "RESULT")
    private String resultNote;
    @Column(name = "AUTHOR")
    private String author;
    @Column(name = "FILE")
    private File file;
    @Column(name = "NAME")
    private String name;
    @Column(name = "TYPES")
    private String types;
    @Lob
    @Column(name = "DATA")
    private byte[] data;
    @Column(name = "DATE")
    private LocalDate date;

    public InstrumentsExaminationEntity() {
    }

    public InstrumentsExaminationEntity(String name, String types, byte[] data) {
        this.name = name;
        this.types = types;
        this.data = data;
    }
}
