package eu.interopehrate.hcpapp.converters.fhir;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationInfoCommand;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Organization;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
public class HapiToCommandDocHistoryConsultation implements Converter<DocumentReference, DocumentHistoryConsultationInfoCommand> {
    @Override
    public DocumentHistoryConsultationInfoCommand convert(DocumentReference source) {
        DocumentHistoryConsultationInfoCommand documentHistoryConsultationInfoCommand = new DocumentHistoryConsultationInfoCommand();
        if (source.hasContent() && source.getContent().get(0).hasAttachment() && source.getContent().get(0).getAttachment().hasCreation()) {
            documentHistoryConsultationInfoCommand.setDate(source.getContent()
                    .get(0)
                    .getAttachment()
                    .getCreation()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        }
        if (source.hasCustodian() && (source.getCustodian().getResource() != null)
                && ((Organization)source.getCustodian().getResource()).hasAddress()
                && ((Organization)source.getCustodian().getResource()).getAddress().get(0).hasCity()
                && ((Organization)source.getCustodian().getResource()).getAddress().get(0).hasCountry()) {
            documentHistoryConsultationInfoCommand.setLocationHospital(((Organization)source.getCustodian().getResource()).getAddress().get(0).getCity()
                    + ", "
                    + ((Organization)source.getCustodian().getResource()).getAddress().get(0).getCountry());
        }
        if (source.hasContent() && source.getContent().get(0).hasAttachment() && source.getContent().get(0).getAttachment().hasTitle()) {
            documentHistoryConsultationInfoCommand.setExam(source.getContent().get(0).getAttachment().getTitle());
        }
        if (source.hasContext() && source.getContext().hasPracticeSetting()
                && source.getContext().getPracticeSetting().hasCoding() && source.getContext().getPracticeSetting().getCoding().get(0).hasDisplay()) {
            documentHistoryConsultationInfoCommand.setSpeciality(source.getContext().getPracticeSetting().getCoding().get(0).getDisplay());
        }
        return documentHistoryConsultationInfoCommand;
    }
}
