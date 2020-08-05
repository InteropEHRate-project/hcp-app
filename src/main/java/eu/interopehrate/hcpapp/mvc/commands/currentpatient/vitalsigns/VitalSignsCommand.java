package eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.DoubleKeyHashMap;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Builder
@Getter
public class VitalSignsCommand {
    private Boolean displayTranslatedVersion;
    private List<VitalSignsInfoCommand> vitalSignsInfoCommands;

    public List<LocalDateTime> localDateTimeListWithoutDuplicates() {
        List<LocalDateTime> withDuplicates = new ArrayList<>();
        for (int i = 0; i < vitalSignsInfoCommands.size(); i++) {
            withDuplicates.add(vitalSignsInfoCommands.get(i).getVitalSignsInfoCommandSample().getLocalDateOfVitalSign());
        }

        List<LocalDateTime> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }

    public List<String> vitalSignsAnalysesWithoutDuplicates() {
        List<String> withDuplicates = new ArrayList<>();
        for (int i = 0; i < vitalSignsInfoCommands.size(); i++) {
            withDuplicates.add(vitalSignsInfoCommands.get(i).getAnalysisName());
        }

        List<String> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }

    public DoubleKeyHashMap valueReturn() {
        DoubleKeyHashMap<String, LocalDateTime, String> mapPair = new DoubleKeyHashMap<>();
        List<String> analysisList = this.vitalSignsAnalysesWithoutDuplicates();
        List<LocalDateTime> dateTimeList = this.localDateTimeListWithoutDuplicates();

        for (int i = 0; i < analysisList.size(); i++) {
            for (int j = 0; j < dateTimeList.size(); j++) {
                for (int k = 0; k < vitalSignsInfoCommands.size(); k++) {
                    if (analysisList.get(i).equals(vitalSignsInfoCommands.get(k).getAnalysisName()) &&
                            (dateTimeList.get(j).equals(vitalSignsInfoCommands.get(k).getVitalSignsInfoCommandSample().getLocalDateOfVitalSign()))) {
                        mapPair.put(analysisList.get(i), dateTimeList.get(j), vitalSignsInfoCommands.get(k).getVitalSignsInfoCommandSample().getCurrentValue() + " " + vitalSignsInfoCommands.get(k).getVitalSignsInfoCommandSample().getUnitOfMeasurement());
                    }
                }
            }
        }
        return mapPair;
    }

}
