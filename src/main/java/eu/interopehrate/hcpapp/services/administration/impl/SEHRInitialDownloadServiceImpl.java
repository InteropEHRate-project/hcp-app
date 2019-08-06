package eu.interopehrate.hcpapp.services.administration.impl;

import eu.interopehrate.hcpapp.converters.EntityToCommandSEHRInitialDownload;
import eu.interopehrate.hcpapp.jpa.entities.SEHRInitialDownloadEntity;
import eu.interopehrate.hcpapp.jpa.repositories.SEHRInitialDownloadRepository;
import eu.interopehrate.hcpapp.mvc.commands.SEHRInitialDownloadCommand;
import eu.interopehrate.hcpapp.services.administration.SEHRInitialDownloadService;
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
    public SEHRInitialDownloadCommand getCurrent() {
        List<SEHRInitialDownloadEntity> all = sehrInitialDownloadRepository.findAll();
        if (CollectionUtils.isEmpty(all)) {
            return new SEHRInitialDownloadCommand();
        } else {
            return entityToCommandSEHRInitialDownload.convert(all.get(0));
        }
    }
}
