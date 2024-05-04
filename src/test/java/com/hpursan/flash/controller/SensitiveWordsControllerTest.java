package com.hpursan.flash.controller;

import com.hpursan.flash.service.SensitiveWordsReplacementService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SensitiveWordsController.class)
public class SensitiveWordsControllerTest {

    @MockBean
    private SensitiveWordsReplacementService sensitiveWordsReplacementService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRedactSensitiveWords_Success() throws Exception {
        String inputMessage = "This is a sensitive message.";
        String redactedMessage = "This is a ******* message.";

        when(sensitiveWordsReplacementService.redactSensitiveWords(inputMessage))
                .thenReturn(redactedMessage);

        mockMvc.perform(post("/api/v1/external")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputMessage))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.redactedMessage").value(redactedMessage));
    }

    @Test
    void testRedactSensitiveWords_Error() throws Exception {
        String inputMessage = "This is a sensitive message.";

        when(sensitiveWordsReplacementService.redactSensitiveWords(anyString()))
                .thenThrow(new RuntimeException());

        mockMvc.perform(post("/api/v1/external")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputMessage))
                .andExpect(status().isBadRequest());
    }
}
