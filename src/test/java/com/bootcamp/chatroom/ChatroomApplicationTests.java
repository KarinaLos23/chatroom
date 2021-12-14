package com.bootcamp.chatroom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class ChatroomApplicationTests {

    @Autowired
    private MessageController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Autowired
    private MockMvc mockMvc;  //mock server

    @Test
    public void shouldReturnErrorWhenNotLoggedIn() throws Exception {
        assertThrows(Exception.class, () -> mockMvc.perform(MockMvcRequestBuilders
                .post("/message")
                .content(asJsonString(new Message("Hello", "token", "Main")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)));
    }

    @Test
    public void shouldReturnMessageWhenLoggedIn() throws Exception {
        LoginResponse loginResponse = controller.login(new LoginRequest("alice"));
        MockHttpServletRequestBuilder request = post("/message")
                .content(asJsonString(new Message("Hello", loginResponse.getUserToken(), "Main")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Hello"));
    }

    public static String asJsonString(final Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

}
