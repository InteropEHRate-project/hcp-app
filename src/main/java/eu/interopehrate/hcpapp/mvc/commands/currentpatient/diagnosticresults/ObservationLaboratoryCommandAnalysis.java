package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;

@Builder
@Getter
public class ObservationLaboratoryCommandAnalysis {
    private Boolean displayTranslatedVersion;
    private List<ObservationLaboratoryInfoCommandAnalysis> observationLaboratoryInfoCommandAnalyses;

    public List<LocalDateTime> localDateTimeListWithoutDuplicates() {

        List<LocalDateTime> withDuplicates = new ArrayList<>();
        for (ObservationLaboratoryInfoCommandAnalysis observationLaboratoryInfoCommandAnalysis : this.observationLaboratoryInfoCommandAnalyses) {
            withDuplicates.add(observationLaboratoryInfoCommandAnalysis.getObservationLaboratoryInfoCommandSample().getSample());
        }
        List<LocalDateTime> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }

    public List<String> observationLaboratoryInfoCommandAnalysesWithoutDuplicates() {
        List<String> withDuplicates = new ArrayList<>();
        for (ObservationLaboratoryInfoCommandAnalysis observationLaboratoryInfoCommandAnalysis : this.observationLaboratoryInfoCommandAnalyses) {
            withDuplicates.add(observationLaboratoryInfoCommandAnalysis.getAnalysis());
        }
        List<String> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }


    public DoubleKeyHashMap valueReturn() {
        DoubleKeyHashMap<String, LocalDateTime, String> mapPair = new DoubleKeyHashMap<>();
        List<String> analysisList = this.observationLaboratoryInfoCommandAnalysesWithoutDuplicates();
        List<LocalDateTime> dateTimeList = this.localDateTimeListWithoutDuplicates();

        for (String s : analysisList) {
            for (LocalDateTime localDateTime : dateTimeList) {
                for (ObservationLaboratoryInfoCommandAnalysis observationLaboratoryInfoCommandAnalysis : this.observationLaboratoryInfoCommandAnalyses) {
                    if (s.equals(observationLaboratoryInfoCommandAnalysis.getAnalysis()) &&
                            (localDateTime.equals(observationLaboratoryInfoCommandAnalysis.getObservationLaboratoryInfoCommandSample().getSample()))) {
                        if (Objects.isNull(observationLaboratoryInfoCommandAnalysis.getObservationLaboratoryInfoCommandSample().getCurrentValue())
                                || Objects.isNull(observationLaboratoryInfoCommandAnalysis.getObservationLaboratoryInfoCommandSample().getUnit())) {
                            mapPair.put(s, localDateTime, "-");
                        } else {
                            mapPair.put(s, localDateTime, observationLaboratoryInfoCommandAnalysis.getObservationLaboratoryInfoCommandSample().getCurrentValue() + " " + observationLaboratoryInfoCommandAnalysis.getObservationLaboratoryInfoCommandSample().getUnit());
                        }
                    }
                }
            }
        }
        return mapPair;
    }

    public boolean getResultOfAnalysis(String analysis, LocalDateTime dateTime) {
        for (ObservationLaboratoryInfoCommandAnalysis el : this.observationLaboratoryInfoCommandAnalyses) {
            if (el.getAnalysis().equals(analysis) && el.getObservationLaboratoryInfoCommandSample().getSample().equals(dateTime)) {
                return el.getIsInLimits();
            }
        }
        return true;
    }

    public String getStringForTooltip(String analysis) {
        for (ObservationLaboratoryInfoCommandAnalysis el : this.observationLaboratoryInfoCommandAnalyses) {
            if (el.getAnalysis().equals(analysis)) {
                return el.getReferenceRange();
            }
        }
        return "No value found";
    }
}
