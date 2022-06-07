package eu.interopehrate.hcpapp.jpa.entities.currentpatient;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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
    @Column(name = "CATEGORY")
    private String category;
    @NotEmpty
    @NotNull
    @Column(name = "NAME")
    private String name;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "SYMPTOMS")
    private String symptoms;
    @Column(name = "COMMENTS")
    private String comments;
    @Column(name = "END_DATE")
    private LocalDate endDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ALLERGY_CODE", nullable = false, foreignKey = @ForeignKey(name = "FK_ALLERGY_CODE"))
    private AllergyTypesEntity allergyTypesEntity;
}
