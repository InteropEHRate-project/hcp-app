package eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata;

import eu.interopehrate.hcpapp.jpa.entities.enums.InstrumentsExamType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstrumentsExaminationInfoCommand {
    private Long id;
    private InstrumentsExamType type;
    private String result;
}
