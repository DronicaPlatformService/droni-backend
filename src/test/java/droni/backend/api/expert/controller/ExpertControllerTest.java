package droni.backend.api.expert.controller;

import droni.backend.api.expert.service.ExpertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class ExpertControllerTest {
    @InjectMocks
    private ExpertController expertController;
    @Mock
    private ExpertService expertService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(expertController).build();
    }

    @Test
    @DisplayName("인기 조종사 반환 성공")
    void expertPopularGet() throws Exception {
        //then
        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders.get("/expert/popular").contentType(MediaType.APPLICATION_JSON));
        perform.andExpect(status().isOk());
    }
}