package eu.interopehrate.hcpapp.mvc.controllers.testd2dlibrary.advice;

import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.mvc.controllers.testd2dlibrary.ExchangeInformationController;
import eu.interopehrate.hcpapp.mvc.controllers.testd2dlibrary.TestD2DLibraryController;
import eu.interopehrate.hcpapp.services.testd2dlibrary.TestD2DLibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {
        TestD2DLibraryController.class,
        ExchangeInformationController.class
})
public class TestD2DLibraryControllersAdvice {
    private static final Logger log = LoggerFactory.getLogger(TestD2DLibraryControllersAdvice.class);
    private TestD2DLibraryService testD2DLibraryService;

    public TestD2DLibraryControllersAdvice(TestD2DLibraryService testD2DLibraryService) {
        this.testD2DLibraryService = testD2DLibraryService;
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception exception, Model model) {
        log.error(exception.getMessage(), exception);
        model.addAttribute("exception", String.format("An error occurred with message - %s. See log for details.", exception.getMessage()));
        model.addAttribute("testD2DLibraryCommand", testD2DLibraryService.currentState());
        return TemplateNames.TEST_D2D_LIBRARY_VIEW;
    }
}
