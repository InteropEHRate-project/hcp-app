package eu.interopehrate.hcpapp.services.currentpatient.impl;

import eu.interopehrate.hcpapp.services.currentpatient.ReasonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReasonServiceImpl implements ReasonService {
    private final List<String> listOfNotes = new ArrayList<>();

    @Override
    public void insertNote(String note) {
        if (note != null && !note.trim().equals("") && !this.listOfNotes.contains(note)) {
            this.listOfNotes.add(note);
        }
    }
}
