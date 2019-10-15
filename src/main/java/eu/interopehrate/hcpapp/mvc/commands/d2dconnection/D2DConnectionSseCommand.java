package eu.interopehrate.hcpapp.mvc.commands.d2dconnection;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class D2DConnectionSseCommand {
    public enum SseCommandAction {
        RELOAD_PAGE, DISPLAY_TEXT
    }

    private SseCommandAction action;
    private String text;
}
