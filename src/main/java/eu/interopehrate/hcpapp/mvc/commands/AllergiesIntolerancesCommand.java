package eu.interopehrate.hcpapp.mvc.commands;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllergiesIntolerancesCommand {
    private List<AllergiesIntolerancesInfoCommand> allergiesIntolerancesInfo;

    public AllergiesIntolerancesCommand(List<AllergiesIntolerancesInfoCommand> allergiesIntolerancesInfo) {
        this.allergiesIntolerancesInfo = allergiesIntolerancesInfo;
    }
}
