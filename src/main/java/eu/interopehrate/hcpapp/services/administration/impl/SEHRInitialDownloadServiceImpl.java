package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.converters.entity.entitytocommand.EntityToCommandSEHRInitialDownload;
import eu.interopehrate.hcpapp.jpa.entities.SEHRInitialDownloadEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.AuditEventType;
import eu.interopehrate.hcpapp.jpa.repositories.SEHRInitialDownloadRepository;
import eu.interopehrate.hcpapp.mvc.commands.administration.SEHRInitialDownloadCommand;
import eu.interopehrate.hcpapp.services.administration.AuditInformationService;
import eu.interopehrate.hcpapp.services.administration.SEHRInitialDownloadService;
import eu.interopehrate.protocols.client.ResourceReader;
import lombok.extern.slf4j.Slf4j;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Iterator;
import java.util.List;

import static eu.interopehrate.protocols.common.DocumentCategory.LABORATORY_REPORT;

@Slf4j
@Service
public class SEHRInitialDownloadServiceImpl implements SEHRInitialDownloadService {
    private final SEHRInitialDownloadRepository sehrInitialDownloadRepository;
    private final EntityToCommandSEHRInitialDownload entityToCommandSEHRInitialDownload;
    private final AuditInformationService auditInformationService;
    private ResourceReader resourceReader;


    public SEHRInitialDownloadServiceImpl(SEHRInitialDownloadRepository sehrInitialDownloadRepository,
                                          EntityToCommandSEHRInitialDownload entityToCommandSEHRInitialDownload,
                                          AuditInformationService auditInformationService) {
        this.sehrInitialDownloadRepository = sehrInitialDownloadRepository;
        this.entityToCommandSEHRInitialDownload = entityToCommandSEHRInitialDownload;
        this.auditInformationService = auditInformationService;
    }

    @Override
    public SEHRInitialDownloadCommand getInitialConfig() {
        List<SEHRInitialDownloadEntity> all = sehrInitialDownloadRepository.findAll();
        return CollectionUtils.isEmpty(all) ? new SEHRInitialDownloadCommand() : entityToCommandSEHRInitialDownload.convert(all.get(0));
    }

    @Override
    public void saveInitialConfig(SEHRInitialDownloadCommand sehrInitialDownloadCommand) {
        List<SEHRInitialDownloadEntity> all = sehrInitialDownloadRepository.findAll();
        SEHRInitialDownloadEntity sehrInitialDownloadEntity = CollectionUtils.isEmpty(all) ? new SEHRInitialDownloadEntity() : all.get(0);
        BeanUtils.copyProperties(sehrInitialDownloadCommand, sehrInitialDownloadEntity);
        sehrInitialDownloadRepository.save(sehrInitialDownloadEntity);
        auditInformationService.auditEvent(AuditEventType.SAVE_INITIAL_SEHR_DOWNLOAD, sehrInitialDownloadCommand.toString());
    }

    @Override
    public Iterator<Resource> getLaboratoryTests() throws Exception {
        return resourceReader.getResourcesByCategory(LABORATORY_REPORT, null, null, null, false);
        // log.info("LaboratoryResults received");
    }
}
