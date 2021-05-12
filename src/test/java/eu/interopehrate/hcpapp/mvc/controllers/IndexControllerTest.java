package eu.interopehrate.hcpapp.mvc.controllers;

import eu.interopehrate.hcpapp.mvc.commands.index.IndexCommand;
import eu.interopehrate.hcpapp.mvc.controllers.index.IndexController;
import eu.interopehrate.hcpapp.services.index.IndexService;
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
class IndexControllerTest {
    @Mock
    private IndexService service;
    @InjectMocks
    private IndexController controller;
    private IndexCommand command;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.command = new IndexCommand();
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.controller).build();
    }

    @Test
    void indexTemplate() throws Exception {
        when(this.service.indexCommand()).thenReturn(this.command);
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(TemplateNames.INDEX_TEMPLATE))
                .andExpect(MockMvcResultMatchers.model().attributeExists("index"));
    }
}