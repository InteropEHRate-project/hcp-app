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
    @NonNull
    private Boolean problems = Boolean.FALSE;
    @NonNull
    private Boolean immunizations = Boolean.FALSE;
    @NonNull
    private Boolean historyOfProcedure = Boolean.FALSE;
    @NonNull
    private Boolean medicalDevices = Boolean.FALSE;
    @NonNull
    private Boolean results = Boolean.FALSE;
    @NonNull
    private Boolean vitalSigns = Boolean.FALSE;
    @NonNull
    private Boolean historyOfPastIllnesses = Boolean.FALSE;
    @NonNull
    private Boolean pregnancy = Boolean.FALSE;
    @NonNull
    private Boolean socialHistory = Boolean.FALSE;
    @NonNull
    private Boolean planOfCare = Boolean.FALSE;
    @NonNull
    private Boolean functionalStatus = Boolean.FALSE;
    @NonNull
    private Boolean advanceDirectives = Boolean.FALSE;
}
