package eu.interopehrate.hcpapp.mvc.commands;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TestD2DLibraryCommand {
    private Boolean on = Boolean.FALSE;
    private String sendActionMessage;
    private String lastSEHRMessage;
}
