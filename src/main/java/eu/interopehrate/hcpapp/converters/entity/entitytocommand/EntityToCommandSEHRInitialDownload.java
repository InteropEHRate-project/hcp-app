package eu.interopehrate.hcpapp.converters.entity.entitytocommand;

import eu.interopehrate.hcpapp.jpa.entities.administration.SEHRInitialDownloadEntity;
import eu.interopehrate.hcpapp.mvc.commands.administration.SEHRInitialDownloadCommand;
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
