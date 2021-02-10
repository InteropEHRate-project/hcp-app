package eu.interopehrate.hcpapp.mvc.controllers.advice;

import eu.interopehrate.hcpapp.mvc.controllers.index.IndexController;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {IndexController.class})
public class IndexControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(IndexControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception, Model model) {
        log.error(exception.getMessage(), exception);
        model.addAttribute("exception", String.format("An error occurred with message - %s. See log for details.", exception.getMessage()));
        return TemplateNames.INDEX_TEMPLATE;
    }
}
