package eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class VitalSignsInfoCommandSample {

    @DateTimeFormat(pattern = "dd/MM/yyyy h:mm a")
    private LocalDateTime localDateOfVitalSign;

    private double currentValue;

    @NotEmpty
    @NotNull
    private String unitOfMeasurement;
}
