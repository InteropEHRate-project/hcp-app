package eu.interopehrate.hcpapp.mvc.controllers.advice.currentpatient.diagnosticresults.observationlaboratory;

import eu.interopehrate.hcpapp.mvc.controllers.currentpatient.diagnosticresults.observationlaboratory.ObservationLaboratoryMediaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(assignableTypes = {ObservationLaboratoryMediaController.class})
public class ObservationLaboratoryMediaControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ObservationLaboratoryMediaControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception, RedirectAttributes redirectAttributes) {
        log.error(exception.getMessage(), exception);
        redirectAttributes.addFlashAttribute("exception", String.format("An error occurred with message - %s. See log for details.", exception.getMessage()));
        return "redirect:/current-patient/diagnostic-results/laboratory-results/observation-laboratory-media-view";
    }
}
