package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.visitdata.ConclusionCommand;
import eu.interopehrate.hcpapp.services.currentpatient.ConclusionService;
import eu.interopehrate.hcpapp.services.currentpatient.CurrentDiseaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ConclusionServiceImpl implements ConclusionService {
    private final CurrentDiseaseService currentDiseaseService;
    private final List<String> listOfConclusionNote = new ArrayList<>();

    public ConclusionServiceImpl(CurrentDiseaseService currentDiseaseService) {
        this.currentDiseaseService = currentDiseaseService;
    }

    @Override
    public ConclusionCommand conclusionComm() {
        return ConclusionCommand.builder()
                .currentDiseaseService(this.currentDiseaseService)
                .build();
    }

    @Override
    public void insertConclusionNote(String conclusionNote) {
        if (conclusionNote != null && !conclusionNote.trim().equals("") && !this.listOfConclusionNote.contains(conclusionNote)) {
            this.listOfConclusionNote.add(conclusionNote);
        }
    }
}
