package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.converters.EntityToCommandSEHRInitialDownload;
import eu.interopehrate.hcpapp.jpa.entities.SEHRInitialDownloadEntity;
import eu.interopehrate.hcpapp.jpa.repositories.SEHRInitialDownloadRepository;
import eu.interopehrate.hcpapp.mvc.commands.SEHRInitialDownloadCommand;
import eu.interopehrate.hcpapp.services.administration.SEHRInitialDownloadService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class SEHRInitialDownloadServiceImpl implements SEHRInitialDownloadService {
    private SEHRInitialDownloadRepository sehrInitialDownloadRepository;
    private EntityToCommandSEHRInitialDownload entityToCommandSEHRInitialDownload;

    public SEHRInitialDownloadServiceImpl(SEHRInitialDownloadRepository sehrInitialDownloadRepository,
                                          EntityToCommandSEHRInitialDownload entityToCommandSEHRInitialDownload) {
        this.sehrInitialDownloadRepository = sehrInitialDownloadRepository;
        this.entityToCommandSEHRInitialDownload = entityToCommandSEHRInitialDownload;
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
    }
}
