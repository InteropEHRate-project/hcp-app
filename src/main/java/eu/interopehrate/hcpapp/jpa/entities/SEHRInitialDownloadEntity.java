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
    private Boolean medicationSummary = Boolean.FALSE;
    @NonNull
    private Boolean allergyIntolerance = Boolean.FALSE;
}
