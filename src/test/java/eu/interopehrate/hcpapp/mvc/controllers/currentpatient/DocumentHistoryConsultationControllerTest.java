package eu.interopehrate.hcpapp.mvc.controllers.currentpatient;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.historyconsultation.DocumentHistoryConsultationCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.DocumentHistoryConsultationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DocumentHistoryConsultationControllerTest {
    @Mock
    private DocumentHistoryConsultationService documentHistoryConsultationService;
    @InjectMocks
    private DocumentHistoryConsultationController documentHistoryConsultationController;
    private DocumentHistoryConsultationCommand documentHistoryConsultationCommand;
    private MockMvc mockMvc;
    private String speciality;

    @BeforeEach
    void setUp() {
        this.documentHistoryConsultationCommand = DocumentHistoryConsultationCommand.builder().build();
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.documentHistoryConsultationController).build();
    }

    @Test
    void viewSection() throws Exception {
        String spec = "{/speciality}";
        when(this.documentHistoryConsultationService.documentHistoryConsultationCommand(speciality)).thenReturn(this.documentHistoryConsultationCommand);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/current-patient/document-history-consultation/view-section/spec"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(TemplateNames.CURRENT_PATIENT_DOCUMENT_HISTORY_CONSULTATION_VIEW_SECTION))
                .andExpect(MockMvcResultMatchers.model().attribute("documentHistoryConsultation",speciality));
    }
}