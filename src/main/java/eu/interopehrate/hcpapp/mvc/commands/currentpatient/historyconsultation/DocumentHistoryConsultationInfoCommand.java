package eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentHistoryConsultationInfoCommand {

    @NotEmpty
    @NotNull
    private String speciality;

    @DateTimeFormat(pattern = "dd/MM/yyyy h:mm a")
    private LocalDateTime localDateTimeHistoryConsultation;

    @NotEmpty
    @NotNull
    private String locationHospital;

    @NotEmpty
    @NotNull
    private String exam;
}
