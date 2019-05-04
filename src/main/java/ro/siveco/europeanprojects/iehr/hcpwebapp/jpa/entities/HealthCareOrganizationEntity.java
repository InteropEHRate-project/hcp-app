package ro.siveco.europeanprojects.iehr.hcpwebapp.jpa.entities;

import lombok.Getter;
import lombok.Setter;
import ro.siveco.europeanprojects.iehr.hcpwebapp.jpa.entities.common.HCPApplicationEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "HEALTH_CARE_ORGANIZATION")
public class HealthCareOrganizationEntity extends HCPApplicationEntity {

    @NotNull
    private String code;
    @NotNull
    private String name;
}
