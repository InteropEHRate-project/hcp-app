package eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

@Builder
@Getter
public class ObservationLaboratoryCommandAnalysis {
    private Boolean displayTranslatedVersion;
    private List<ObservationLaboratoryInfoCommandAnalysis> observationLaboratoryInfoCommandAnalyses;

    //PAGE SIZE is hardcoded HERE
    private static int pageSize = 10;
    private static int totalPages;
    private static long totalElements;

    public static int getTotalPages() {
        return totalPages;
    }

    public static long getTotalElements() {
        return totalElements;
    }

    public void createPagination(int pageNo) {
        //Pagination generation
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<String> page = createPageFromList(this.noDuplicatesList(), pageable, pageNo, pageSize);
        totalElements = page.getTotalElements();
        totalPages = page.getTotalPages();
    }

    public List<LocalDateTime> localDateTimeListWithoutDuplicates() {
        List<LocalDateTime> withDuplicates = new ArrayList<>();
        this.observationLaboratoryInfoCommandAnalyses.forEach(obs -> withDuplicates.add(obs.getObservationLaboratoryInfoCommandSample().getSample()));
        List<LocalDateTime> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }

    public List<String> observationLaboratoryInfoCommandAnalysesWithoutDuplicates(int pageNo) {
        //Pagination generation
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<String> page = createPageFromList(this.noDuplicatesList(), pageable, pageNo, pageSize);
        return page.getContent();
    }


    public DoubleKeyHashMap valueReturn(int pageNo) {
        DoubleKeyHashMap<String, LocalDateTime, String> mapPair = new DoubleKeyHashMap<>();
        List<String> analysisList = this.observationLaboratoryInfoCommandAnalysesWithoutDuplicates(pageNo);
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

    private static Page<String> createPageFromList(List<String> list, Pageable pageable, int pageNo, int pageSize) {
        try {
            int index = (pageNo - 1) * pageSize;
            return new PageImpl<>(list.subList(index, index + pageSize), pageable, list.size());
        } catch (IndexOutOfBoundsException ignored) {
            int index = (pageNo - 1) * pageSize;
            return new PageImpl<>(list.subList(index, list.size()), pageable, list.size());
        }
    }

    private List<String> noDuplicatesList() {
        List<String> withDuplicates = new ArrayList<>();
        this.observationLaboratoryInfoCommandAnalyses.forEach(obs -> withDuplicates.add(obs.getAnalysis()));
        List<String> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }
}
