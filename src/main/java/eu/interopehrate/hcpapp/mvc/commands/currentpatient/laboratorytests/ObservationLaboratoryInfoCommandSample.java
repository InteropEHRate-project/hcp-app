package eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class ObservationLaboratoryInfoCommandSample {
    @NotEmpty
    @NotNull
    private LocalDateTime sample = LocalDateTime.now().minusDays(2);
    @NotEmpty
    @NotNull
    private Double currentValue;
    private String unit = "-";
    private double upperLimitBound = Double.MAX_VALUE;
    private double lowerLimitBound = Double.MIN_VALUE;
    @NotEmpty
    @NotNull
    @DateTimeFormat(pattern = "dd-MM-yyyy h:mm a")
    private LocalDateTime localDateOfLaboratory = LocalDateTime.now();
    @NotEmpty
    @NotNull
    private String unitOfMeasurement;
}
