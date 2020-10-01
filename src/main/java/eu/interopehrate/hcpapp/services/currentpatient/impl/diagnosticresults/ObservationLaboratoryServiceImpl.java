package eu.interopehrate.hcpapp.services.currentpatient.impl.diagnosticresults;

import eu.interopehrate.hcpapp.converters.fhir.diagnosticresults.HapiToCommandObservationLaboratory;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryCommandAnalysis;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.diagnosticresults.ObservationLaboratoryInfoCommandAnalysis;
import eu.interopehrate.hcpapp.services.currentpatient.diagnosticresults.ObservationLaboratoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ObservationLaboratoryServiceImpl implements ObservationLaboratoryService {
    private CurrentPatient currentPatient;
    private HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory;

    public ObservationLaboratoryServiceImpl(CurrentPatient currentPatient, HapiToCommandObservationLaboratory hapiToCommandObservationLaboratory) {
        this.currentPatient = currentPatient;
        this.hapiToCommandObservationLaboratory = hapiToCommandObservationLaboratory;
    }

    @Override
    public ObservationLaboratoryCommandAnalysis observationLaboratoryInfoCommandAnalysis(int pageNo, int pageSize) {
        var observationLaboratoryInfoCommandAnalyses = currentPatient.laboratoryList()
                .stream()
                .map(hapiToCommandObservationLaboratory::convert)
//                .sorted(Comparator.comparing(ObservationLaboratoryInfoCommandAnalysis::getAnalysis))
                .collect(Collectors.toList());

        observationLaboratoryInfoCommandAnalyses.forEach(ObservationLaboratoryInfoCommandAnalysis::setIsInLimits);

        //Pagination generation
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<ObservationLaboratoryInfoCommandAnalysis> page = createPageFromList(observationLaboratoryInfoCommandAnalyses, pageable, pageNo, pageSize);
        return ObservationLaboratoryCommandAnalysis.builder()
                .displayTranslatedVersion(currentPatient.getDisplayTranslatedVersion())
                .observationLaboratoryInfoCommandAnalyses(page)
                .build();
    }

    private static Page<ObservationLaboratoryInfoCommandAnalysis> createPageFromList(List<ObservationLaboratoryInfoCommandAnalysis> list, Pageable pageable, int pageNo, int pageSize) {
        try {
            int index = (pageNo - 1) * pageSize;
            return new PageImpl<>(list.subList(index, index + pageSize), pageable, list.size());
        } catch (IndexOutOfBoundsException ignored) {
            int index = (pageNo - 1) * pageSize;
            return new PageImpl<>(list.subList(index, list.size()), pageable, list.size());
        }
    }
}
