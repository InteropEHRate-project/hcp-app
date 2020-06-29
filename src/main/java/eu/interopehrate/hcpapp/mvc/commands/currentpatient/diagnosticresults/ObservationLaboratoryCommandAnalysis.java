package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Builder
@Getter
public class ObservationLaboratoryCommandAnalysis {
    private Boolean displayTranslatedVersion;
    private List<ObservationLaboratoryInfoCommandAnalysis> observationLaboratoryInfoCommandAnalyses;


    public List<LocalDateTime> localDateTimeListWithoutDuplicates(){

        List<LocalDateTime> withDuplicates = new ArrayList<>();
        for (int i = 0; i < observationLaboratoryInfoCommandAnalyses.size(); i++){
            withDuplicates.add(observationLaboratoryInfoCommandAnalyses.get(i).getObservationLaboratoryInfoCommandSample().getSample());
        }

        List<LocalDateTime> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }

    public List<String> observationLaboratoryInfoCommandAnalysesWithoutDuplicates(){
        List<String> withDuplicates = new ArrayList<>();
        for (int i = 0; i < observationLaboratoryInfoCommandAnalyses.size(); i++){
            withDuplicates.add(observationLaboratoryInfoCommandAnalyses.get(i).getAnalysis());
        }

        List<String> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }


    public DoubleKeyHashMap valueReturn (){
        DoubleKeyHashMap<String,LocalDateTime,String> mapPair = new DoubleKeyHashMap<>();
        List<String> analysisList = this.observationLaboratoryInfoCommandAnalysesWithoutDuplicates();
        List<LocalDateTime> dateTimeList = this.localDateTimeListWithoutDuplicates();

        for (int i = 0; i < analysisList.size(); i++){
            for (int j = 0; j < dateTimeList.size(); j++){
                for(int k=0; k < observationLaboratoryInfoCommandAnalyses.size(); k++){
                    if(analysisList.get(i).equals(observationLaboratoryInfoCommandAnalyses.get(k).getAnalysis()) &&
                            (dateTimeList.get(j).equals(observationLaboratoryInfoCommandAnalyses.get(k).getObservationLaboratoryInfoCommandSample().getSample()))) {
                             mapPair.put(analysisList.get(i),dateTimeList.get(j),observationLaboratoryInfoCommandAnalyses.get(k).getObservationLaboratoryInfoCommandSample().getCurrentValue()+" " + observationLaboratoryInfoCommandAnalyses.get(k).getObservationLaboratoryInfoCommandSample().getUnit());
                    }
                }
            }
        }

        return mapPair;

    }



}
