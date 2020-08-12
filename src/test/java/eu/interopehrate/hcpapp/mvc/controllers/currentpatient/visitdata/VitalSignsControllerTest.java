package eu.interopehrate.hcpapp.mvc.controllers.currentpatient.visitdata;

import eu.interopehrate.hcpapp.mvc.commands.currentpatient.vitalsigns.VitalSignsCommand;
import eu.interopehrate.hcpapp.mvc.controllers.TemplateNames;
import eu.interopehrate.hcpapp.services.currentpatient.VitalSignsService;
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

import java.io.IOException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VitalSignsControllerTest {
    @Mock
    private VitalSignsService vitalSignsService;
    @InjectMocks
    private VitalSignsController vitalSignsController;
    private VitalSignsCommand vitalSignsCommand;
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        this.vitalSignsCommand = VitalSignsCommand.builder().build();
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.vitalSignsController).build();
    }

    @Test
    void viewSection() throws Exception {
        when(this.vitalSignsService.vitalSignsCommand()).thenReturn(this.vitalSignsCommand);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/current-patient/visit-data/vital-signs/view-section"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(TemplateNames.CURRENT_PATIENT_VITAL_SIGNS_VIEW_SECTION))
                .andExpect(MockMvcResultMatchers.model().attributeExists("vitalSigns"));
    }
}