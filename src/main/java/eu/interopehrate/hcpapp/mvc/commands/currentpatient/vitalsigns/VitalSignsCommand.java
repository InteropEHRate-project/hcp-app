package eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.AnalysisWrapper;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests.DoubleKeyHashMap;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

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

    public List<AnalysisWrapper> vitalSignsAnalysesWithoutDuplicatesSEHR() {
        return this.noDuplicatesList();
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

    public DoubleKeyHashMap valueReturn1() {
        DoubleKeyHashMap<AnalysisWrapper, LocalDateTime, String> mapPair = new DoubleKeyHashMap<>();
        List<AnalysisWrapper> analysisList = this.vitalSignsAnalysesWithoutDuplicatesSEHR();
        List<LocalDateTime> dateTimeList = this.localDateTimeListWithoutDuplicates();

        analysisList.forEach(an -> dateTimeList.forEach(date -> this.getVitalSignsInfoCommands().forEach(obs -> {
            if (an.getAnalysis().equals(obs.getAnalysisName()) &&
                    (date.equals(obs.getVitalSignsInfoCommandSample().getLocalDateOfVitalSign()))) {
                if (Objects.isNull(obs.getVitalSignsInfoCommandSample().getCurrentValue())
                        || Objects.isNull(obs.getVitalSignsInfoCommandSample().getUnitOfMeasurement())) {
                    mapPair.put(an, date, "-");
                } else {
                    mapPair.put(an, date, obs.getVitalSignsInfoCommandSample().getCurrentValue() + " " + obs.getVitalSignsInfoCommandSample().getUnitOfMeasurement());
                }
            }
        })));
        return mapPair;
    }

    private List<AnalysisWrapper> noDuplicatesList() {
        List<AnalysisWrapper> withDuplicates = new ArrayList<>();

        for (VitalSignsInfoCommand o : this.getVitalSignsInfoCommands()) {
            AnalysisWrapper analysisWrapper = new AnalysisWrapper();
            analysisWrapper.setAnalysis(o.getAnalysisName());
            analysisWrapper.setAnalysisTranslated(o.getAnalysisNameTranslated());
            withDuplicates.add(analysisWrapper);
        }

        List<AnalysisWrapper> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        noDuplicates.sort(new Comparator<AnalysisWrapper>() {
            @Override
            public int compare(AnalysisWrapper o1, AnalysisWrapper o2) {
                return o1.getAnalysis().compareTo(o2.getAnalysis());
            }
        });
        return noDuplicates;
    }
}
