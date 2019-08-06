package eu.interopehrate.hcpapp.converters;

import eu.interopehrate.hcpapp.jpa.entities.SEHRInitialDownloadEntity;
import eu.interopehrate.hcpapp.mvc.commands.SEHRInitialDownloadCommand;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToCommandSEHRInitialDownload implements Converter<SEHRInitialDownloadEntity, SEHRInitialDownloadCommand> {
    @Override
    public SEHRInitialDownloadCommand convert(SEHRInitialDownloadEntity sehrInitialDownloadEntity) {
        SEHRInitialDownloadCommand command = new SEHRInitialDownloadCommand();
        BeanUtils.copyProperties(sehrInitialDownloadEntity, command);
        return command;
    }
}
