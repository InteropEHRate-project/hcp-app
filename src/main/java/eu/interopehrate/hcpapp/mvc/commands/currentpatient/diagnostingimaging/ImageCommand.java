package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnostingimaging;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ImageCommand {
    private List<ImageInfoCommand> imageInfoCommands;
}
