package com.example.user.controller;

import com.example.user.domain.User;
import com.example.user.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerItTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    /**
     * This test checks if the POST request on the /user URL returns the expected user and 201 status
     *
     * @throws Exception exception
     */
    @Test
    public void createUser_shouldSuccess() throws Exception {
        Long userId = 1L;
        String birthdateValue = "1999-12-11";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth = LocalDate.parse(birthdateValue, formatter);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(User.builder().id(userId).username("Bob").country("France").birthdate(birth).build())))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        User user = objectMapper.readValue(response.getContentAsString(), User.class);
        assertEquals(response.getStatus(), HttpStatus.CREATED.value());
        assertThat(response, is(notNullValue()));
        assertEquals(user.getId(), userId);
        assertEquals(userService.getUser(userId).get().getUsername(), "Bob");
        // A get request to verify the registration of the user after a post request
        MvcResult result_get = mvc.perform(get("/user/{id}", userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = result_get.getResponse().getContentAsString();
        User useradd = objectMapper.readValue(content, User.class);
        assertEquals(useradd.getId(), userId);
        assertEquals(useradd.getUsername(), "Bob");
    }

    /**
     * This test checks if the POST request on the /user URL returns 400 status when adding a user without birthdate (required field)
     *
     * @throws Exception exception
     */
    @Test
    public void createUserWithoutBirthdate_shouldReturn400() throws Exception {
        Long userId = 1L;
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(User.builder().id(userId).username("Bob").country("France").build())))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
    }

    /**
     * This test checks if the Post Request on the /user URL returns 400 status when adding a user that already exists (same username)
     *
     * @throws Exception exception
     */
    @Test
    public void createUserAlreadyExists_shouldReturn400() throws Exception {
        Long userId = 1L;
        String birthdateValue = "1999-12-11";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth = LocalDate.parse(birthdateValue, formatter);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(User.builder().id(userId).username("Bob").country("France").birthdate(birth).build())))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        User user = objectMapper.readValue(response.getContentAsString(), User.class);
        assertEquals(response.getStatus(), HttpStatus.CREATED.value());
        assertEquals(user.getId(), userId);
        assertEquals(userService.getUser(userId).get().getUsername(), "Bob");

        MvcResult resultex = mvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(User.builder().id(2L).username("Bob").country("France").birthdate(birth).build())))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse resp = resultex.getResponse();
        assertEquals(resp.getStatus(), HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void createUserNotFrench_shouldReturn400() throws Exception {
        Long userId = 1L;
        String birthdateValue = "1999-12-11";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth = LocalDate.parse(birthdateValue, formatter);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(User.builder().id(userId).username("Alice").birthdate(birth).country("Swiss").build())))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
    }

}
