package eu.interopehrate.hcpapp.mvc.commands;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SEHRInitialDownloadCommand {
    private Boolean medicationSummary = Boolean.FALSE;
    private Boolean allergyIntolerance = Boolean.FALSE;
}
