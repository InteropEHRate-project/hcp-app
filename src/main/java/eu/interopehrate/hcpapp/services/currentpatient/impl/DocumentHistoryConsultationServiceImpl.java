package eu.interopehrate.hcpapp.services.currentpatient.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import eu.interopehrate.hcpapp.converters.fhir.HapiToCommandDocHistoryConsultation;
import eu.interopehrate.hcpapp.currentsession.BundleProcessor;
import eu.interopehrate.hcpapp.currentsession.CurrentPatient;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationCommand;
import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationInfoCommand;
import eu.interopehrate.hcpapp.services.currentpatient.DocumentHistoryConsultationService;
import lombok.SneakyThrows;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentHistoryConsultationServiceImpl implements DocumentHistoryConsultationService {
    private final CurrentPatient currentPatient;
    private final Bundle docHistoryConsult;
    private final HapiToCommandDocHistoryConsultation hapiToCommandDocHistoryConsultation;
    private boolean isFiltered = false;
    private boolean isEmpty = false;

    @SneakyThrows
    public DocumentHistoryConsultationServiceImpl(CurrentPatient currentPatient, HapiToCommandDocHistoryConsultation hapiToCommandDocHistoryConsultation) {
        this.currentPatient = currentPatient;
        this.hapiToCommandDocHistoryConsultation = hapiToCommandDocHistoryConsultation;

        File json = new ClassPathResource("MedicalDocumentReferenceExampleBundle.json").getFile();
        FileInputStream file = new FileInputStream(json);
        String lineReadtest = readFromInputStream(file);
        IParser parser = FhirContext.forR4().newJsonParser();
        this.docHistoryConsult = parser.parseResource(Bundle.class, lineReadtest);
        this.currentPatient.initDocHistoryConsultation(this.docHistoryConsult);
    }

    @Override
    public boolean isFiltered() {
        return isFiltered;
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override
    public DocumentHistoryConsultationCommand documentHistoryConsultationCommand(String speciality) {
        var docHistoryConsultationCommands = new BundleProcessor(this.docHistoryConsult).docHistoryConsultationList()
                .stream()
                .map(this.hapiToCommandDocHistoryConsultation::convert)
                .collect(Collectors.toList());

        if (speciality.equalsIgnoreCase("all")) {
            this.isFiltered = false;
            this.isEmpty = false;
            return DocumentHistoryConsultationCommand.builder()
                    .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                    .documentHistoryConsultationInfoCommandList(docHistoryConsultationCommands)
                    .build();
        }
        this.isEmpty = false;
        if (!docHistoryConsultationCommands.isEmpty() && filter(docHistoryConsultationCommands, speciality).isEmpty()) {
            this.isEmpty = true;
            this.isFiltered = false;
        }
        if (!docHistoryConsultationCommands.isEmpty() && !filter(docHistoryConsultationCommands, speciality).isEmpty()) {
            this.isFiltered = true;
        }
        return DocumentHistoryConsultationCommand.builder()
                .displayTranslatedVersion(this.currentPatient.getDisplayTranslatedVersion())
                .documentHistoryConsultationInfoCommandList(filter(docHistoryConsultationCommands, speciality))
                .build();
    }

    @Override
    public List<DocumentHistoryConsultationInfoCommand> filterByDate(List<DocumentHistoryConsultationInfoCommand> list, String style) {
        List<DocumentHistoryConsultationInfoCommand> returnedList = new ArrayList<>();
        if (!style.equals("all")) {
            if (style.equals("last-year")) {
                for (DocumentHistoryConsultationInfoCommand doc : list) {
                    if (LocalDate.now().getYear() - doc.getDate().getYear() <= 1) {
                        returnedList.add(doc);
                    }
                }
                if (!returnedList.isEmpty()) {
                    this.isEmpty = false;
                    this.isFiltered = true;
                } else {
                    this.isEmpty = true;
                    this.isFiltered = false;
                }
                return returnedList;
            }
            if (style.equals("last-5-years")) {
                for (DocumentHistoryConsultationInfoCommand doc : list) {
                    if (LocalDate.now().getYear() - doc.getDate().getYear() <= 5) {
                        returnedList.add(doc);
                    }
                }
                if (!returnedList.isEmpty()) {
                    this.isEmpty = false;
                    this.isFiltered = true;
                } else {
                    this.isEmpty = true;
                    this.isFiltered = false;
                }
                return returnedList;
            }
            this.isFiltered = false;
            this.isEmpty = false;
        }
        return list;
    }

    private static List<DocumentHistoryConsultationInfoCommand> filter(List<DocumentHistoryConsultationInfoCommand> list, String speciality) {
        List<DocumentHistoryConsultationInfoCommand> documentHistoryConsultationInfoCommands = new ArrayList<>();
        list.forEach(dc -> {
            if (dc.getSpeciality().equalsIgnoreCase(speciality)) {
                documentHistoryConsultationInfoCommands.add(dc);
            }
        });
        return documentHistoryConsultationInfoCommands;
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
