package eu.interopehrate.hcpapp.jpa.entities;

import eu.interopehrate.hcpapp.jpa.entities.common.HCPApplicationEntity;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "SEHR_INITIAL_DOWNLOAD")
public class SEHRInitialDownloadEntity extends HCPApplicationEntity {
    @NonNull
    private Boolean currentDiseases = Boolean.FALSE;
    @NonNull
    private Boolean patHistory = Boolean.FALSE;
    @NonNull
    private Boolean allergies = Boolean.FALSE;
    @NonNull
    private Boolean currentMedication = Boolean.FALSE;
    @NonNull
    private Boolean documentHistoryConsultation = Boolean.FALSE;
    @NonNull
    private Boolean laboratoryTests = Boolean.FALSE;

}
