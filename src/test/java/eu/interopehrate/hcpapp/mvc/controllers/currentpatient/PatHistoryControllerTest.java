package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.pathistory.PatHistoryCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.PatHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatHistoryControllerTest {
    @Mock
    private PatHistoryService patHistoryService;
    @InjectMocks
    private PatHistoryController patHistoryController;
    private PatHistoryCommand patHistoryCommand;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.patHistoryCommand = PatHistoryCommand.builder().build();
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.patHistoryController).build();
    }

    @Test
    void viewSection() throws Exception {
        when(this.patHistoryService.patHistorySection()).thenReturn(this.patHistoryCommand);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/current-patient/pat-history/view-section"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(TemplateNames.CURRENT_PATIENT_PAT_HISTORY_VIEW_SECTION))
                .andExpect(MockMvcResultMatchers.model().attributeExists("patHistoryCommand"));
    }
}