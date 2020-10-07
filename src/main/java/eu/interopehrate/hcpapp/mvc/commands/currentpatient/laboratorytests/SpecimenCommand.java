package eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SpecimenCommand {
    private Boolean displayTranslatedVersion;
    private List<SpecimenInfoCommand> specimenInfoCommandList;
}
