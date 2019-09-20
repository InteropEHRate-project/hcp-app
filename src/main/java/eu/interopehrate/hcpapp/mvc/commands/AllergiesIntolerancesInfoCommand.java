package eu.interopehrate.hcpapp.mvc.commands;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AllergiesIntolerancesInfoCommand {
        private String identifier;
        private String name;
        private String clinicalStatus;
        private String type;
        private String category;
        private String criticality;
}
