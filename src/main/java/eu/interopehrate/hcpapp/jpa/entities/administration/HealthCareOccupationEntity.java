package eu.interopehrate.hcpapp.jpa.entities.administration;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "HEALTH_CARE_OCCUPATIONS")
public class HealthCareOccupationEntity extends HCPApplicationEntity {
    @NotNull
    @Column(name = "NAME")
    private String name;
    @ManyToOne(optional = false)
    @JoinColumn(name = "GROUP_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_OCCUPATION_OCCUPATION_GROUP"))
    private HealthCareOccupationGroupEntity occupationGroup;
}
