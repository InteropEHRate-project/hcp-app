package eu.interopehrate.hcpapp.jpa.entities.currentpatient;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "ALLERGIES")
public class AllergyEntity extends HCPApplicationEntity {
    private String patientId;
    private String author;
    @Column(name = "IDENTIFIER")
    private String identifier;
    @NotEmpty
    @NotNull
    @Column(name = "CATEGORY")
    private String category;
    @NotEmpty
    @NotNull
    @Column(name = "NAME")
    private String name;
    @NotEmpty
    @NotNull
    @Column(name = "TYPE")
    private String type;
    @NotEmpty
    @NotNull
    @Column(name = "SYMPTOMS")
    private String symptoms;
    @Column(name = "COMMENTS")
    private String comments;
    @Column(name = "END_DATE")
    private LocalDate endDate;
}
