package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.*;

@Builder
@Getter
public class ObservationLaboratoryCommandAnalysis {
    private Boolean displayTranslatedVersion;
    private Page<ObservationLaboratoryInfoCommandAnalysis> observationLaboratoryInfoCommandAnalyses;

    public List<LocalDateTime> localDateTimeListWithoutDuplicates() {

        List<LocalDateTime> withDuplicates = new ArrayList<>();
        this.observationLaboratoryInfoCommandAnalyses.forEach(obs -> withDuplicates.add(obs.getObservationLaboratoryInfoCommandSample().getSample()));
        List<LocalDateTime> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }

    public List<String> observationLaboratoryInfoCommandAnalysesWithoutDuplicates() {
        List<String> withDuplicates = new ArrayList<>();
        this.observationLaboratoryInfoCommandAnalyses.forEach(obs -> withDuplicates.add(obs.getAnalysis()));
        List<String> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }


    public DoubleKeyHashMap valueReturn() {
        DoubleKeyHashMap<String, LocalDateTime, String> mapPair = new DoubleKeyHashMap<>();
        List<String> analysisList = this.observationLaboratoryInfoCommandAnalysesWithoutDuplicates();
        List<LocalDateTime> dateTimeList = this.localDateTimeListWithoutDuplicates();

        analysisList.forEach(an -> dateTimeList.forEach(date -> this.observationLaboratoryInfoCommandAnalyses.forEach(obs -> {
            if (an.equals(obs.getAnalysis()) &&
                    (date.equals(obs.getObservationLaboratoryInfoCommandSample().getSample()))) {
                if (Objects.isNull(obs.getObservationLaboratoryInfoCommandSample().getCurrentValue())
                        || Objects.isNull(obs.getObservationLaboratoryInfoCommandSample().getUnit())) {
                    mapPair.put(an, date, "-");
                } else {
                    mapPair.put(an, date, obs.getObservationLaboratoryInfoCommandSample().getCurrentValue() + " " + obs.getObservationLaboratoryInfoCommandSample().getUnit());
                }
            }
        })));
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
