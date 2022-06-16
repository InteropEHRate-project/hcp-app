package eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests;

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
    private final Boolean displayTranslatedVersion;
    private final List<ObservationLaboratoryInfoCommandAnalysis> observationLaboratoryInfoCommandAnalyses;

    //PAGE SIZE is hardcoded HERE
    private static final int pageSize = 10;
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
        Page<AnalysisWrapper> page = createPageFromList(this.noDuplicatesList(), pageable, pageNo);
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

    public List<AnalysisWrapper> observationLaboratoryInfoCommandAnalysesWithoutDuplicates(int pageNo) {
        //Pagination generation
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<AnalysisWrapper> page = createPageFromList(this.noDuplicatesList(), pageable, pageNo);
        return page.getContent();
    }

    @SuppressWarnings("rawtypes")
    public DoubleKeyHashMap valueReturn(int pageNo) {
        DoubleKeyHashMap<AnalysisWrapper, LocalDateTime, String> mapPair = new DoubleKeyHashMap<>();
        List<AnalysisWrapper> analysisList = this.observationLaboratoryInfoCommandAnalysesWithoutDuplicates(pageNo);
        List<LocalDateTime> dateTimeList = this.localDateTimeListWithoutDuplicates();

        analysisList.forEach(an -> dateTimeList.forEach(date -> this.observationLaboratoryInfoCommandAnalyses.forEach(obs -> {
            if (an.getAnalysis().equals(obs.getAnalysis()) &&
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

    public List<String> laboratoryAnalysesWithoutDuplicates() {
        List<String> withDuplicates = new ArrayList<>();
        this.observationLaboratoryInfoCommandAnalyses.forEach(laboratory -> withDuplicates.add(laboratory.getAnalysisName()));

        List<String> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }

    public List<LocalDateTime> localDateTimeListWithoutDuplicatesLab() {
        List<LocalDateTime> withDuplicates = new ArrayList<>();
        this.observationLaboratoryInfoCommandAnalyses.forEach(laboratory -> withDuplicates.add(laboratory.getObservationLaboratoryInfoCommandSample().getLocalDateOfLaboratory()));

        List<LocalDateTime> noDuplicates = new ArrayList<>(new HashSet<>(withDuplicates));
        Collections.sort(noDuplicates);
        return noDuplicates;
    }

    @SuppressWarnings("rawtypes")
    public DoubleKeyHashMap valueReturnLaboratory() {
        DoubleKeyHashMap<String, LocalDateTime, String> mapPair = new DoubleKeyHashMap<>();
        List<String> analysisList = this.laboratoryAnalysesWithoutDuplicates();
        List<LocalDateTime> dateTimeList = this.localDateTimeListWithoutDuplicates();

        analysisList.forEach(an -> dateTimeList.forEach(date -> this.observationLaboratoryInfoCommandAnalyses.forEach(laboratory -> {
            if (an.equals(laboratory.getAnalysisName())) {
                mapPair.put(an, laboratory.getObservationLaboratoryInfoCommandSample().getLocalDateOfLaboratory(), laboratory.getObservationLaboratoryInfoCommandSample().getCurrentValue() + " " + laboratory.getObservationLaboratoryInfoCommandSample().getUnitOfMeasurement());
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

    private static Page<AnalysisWrapper> createPageFromList(List<AnalysisWrapper> list, Pageable pageable, int pageNo) {
        try {
            int index = (pageNo - 1) * ObservationLaboratoryCommandAnalysis.pageSize;
            return new PageImpl(list.subList(index, index + ObservationLaboratoryCommandAnalysis.pageSize), pageable, list.size());
        } catch (IndexOutOfBoundsException ignored) {
            int index = (pageNo - 1) * ObservationLaboratoryCommandAnalysis.pageSize;
            return new PageImpl(list.subList(index, list.size()), pageable, list.size());
        }
    }

    private List<AnalysisWrapper> noDuplicatesList() {
        List<AnalysisWrapper> withDuplicates = new ArrayList<>();

        for (ObservationLaboratoryInfoCommandAnalysis o : this.observationLaboratoryInfoCommandAnalyses) {
            AnalysisWrapper analysisWrapper = new AnalysisWrapper();
            analysisWrapper.setAnalysis(o.getAnalysis());
            analysisWrapper.setAnalysisTranslated(o.getAnalysisTranslated());
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
