package eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.DoubleKeyHashMap;
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
    private final Boolean displayTranslatedVersion;
    private final List<VitalSignsInfoCommand> vitalSignsInfoCommands;

    public List<LocalDateTime> localDateTimeListWithoutDuplicates() {
        List<LocalDateTime> withDuplicates = new ArrayList<>();
        this.vitalSignsInfoCommands.forEach(vital -> withDuplicates.add(vital.getVitalSignsInfoCommandSample().getLocalDateOfVitalSign()));

        List<LocalDateTime> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }

    public List<String> vitalSignsAnalysesWithoutDuplicates() {
        List<String> withDuplicates = new ArrayList<>();
        this.vitalSignsInfoCommands.forEach(vital -> withDuplicates.add(vital.getAnalysisName()));

        List<String> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }

    @SuppressWarnings("rawtypes")
    public DoubleKeyHashMap valueReturn() {
        DoubleKeyHashMap<String, LocalDateTime, String> mapPair = new DoubleKeyHashMap<>();
        List<String> analysisList = this.vitalSignsAnalysesWithoutDuplicates();
        List<LocalDateTime> dateTimeList = this.localDateTimeListWithoutDuplicates();

        analysisList.forEach(an -> dateTimeList.forEach(date -> this.vitalSignsInfoCommands.forEach(vital -> {
            if (an.equals(vital.getAnalysisName()) && (date.equals(vital.getVitalSignsInfoCommandSample().getLocalDateOfVitalSign()))) {
                mapPair.put(an, date, vital.getVitalSignsInfoCommandSample().getCurrentValue() + " " + vital.getVitalSignsInfoCommandSample().getUnitOfMeasurement());
            }
        })));
        return mapPair;
    }
}
