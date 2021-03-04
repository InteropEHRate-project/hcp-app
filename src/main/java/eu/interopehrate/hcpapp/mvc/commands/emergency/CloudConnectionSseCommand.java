package eu.interopehrate.hcpapp.mvc.commands.emergency;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CloudConnectionSseCommand {
    public enum SseCommandAction {
        RELOAD_PAGE, DISPLAY_TEXT
    }

    private final SseCommandAction action;
    private final String text;
}