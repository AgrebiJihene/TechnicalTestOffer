package com.example.user.controller;

import com.example.user.controllers.UserController;
import com.example.user.domain.Gender;
import com.example.user.domain.User;
import com.example.user.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit Tests for the REST Controller
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)

public class UserControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    @MockBean
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    /**
     * because the controller methods use this class: UserService
     */
    @MockBean
    private UserService service;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * This test checks if the GET request on the /user/{id} URL returns a user and a 200 status
     *
     * @throws Exception exception
     */
    @Test
    public void shouldReturnUser() throws Exception {
        long ID = 1L;
        String birthdate = "1999-11-10";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth = LocalDate.parse(birthdate, formatter);
        User user = new User(ID, "Jihane", birth, "France", "15608658", Gender.FEMALE);
        when(this.service.getUser(ID)).thenReturn(Optional.of(user));
        MvcResult result = mockMvc.perform(get("/user/{id}", ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        User response = objectMapper.readValue(content, User.class);
        assertEquals(response.getId(), user.getId());
        assertEquals(response.getUsername(), user.getUsername());
        assertEquals(response.getBirthdate(), user.getBirthdate());
        assertEquals(response.getCountry(), user.getCountry());
        assertEquals(response.getPhone(), user.getPhone());
        assertEquals(response.getGender(), user.getGender());
    }

    /**
     * This test checks if the GET request on the /user/{id} URL returns a 404 status
     *
     * @throws Exception exception
     */
    @Test
    public void shouldReturnNotFoundUser() throws Exception {
        long id = 100L;
        when(this.service.getUser(id)).thenReturn(Optional.empty());
        mockMvc.perform(get("/user/{id}", id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    /**
     * This test checks if the POST request on the /user URL returns the expected user and 201 status
     *
     * @throws Exception exception
     */
    @Test
    public void registerUser_shouldSuccess() throws Exception {
        String birthdateValue = "1999-12-11";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth = LocalDate.parse(birthdateValue, formatter);
        User user = new User(2L, "Jihane", birth, "France", "15608658", Gender.FEMALE);
        when(service.registerNewUser(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        MvcResult result = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andReturn();
        MockHttpServletResponse response = result.getResponse();
        User useradd = objectMapper.readValue(response.getContentAsString(), User.class);
        assertThat(response, is(notNullValue()));
        assertEquals(response.getStatus(), HttpStatus.CREATED.value());
        assertEquals(useradd.getId(), user.getId());
    }

    /**
     * This test checks if the POST request on the /user URL returns a 400 status when adding a user without username
     *
     * @throws Exception exception
     */
    @Test
    public void registerUser_shouldReturn400() throws Exception {
        String birthdateValue = "1970-12-14";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth = LocalDate.parse(birthdateValue, formatter);
        User userWithoutUsername = new User(3L, null, birth, "France", "328172", Gender.MALE);
        MvcResult result = mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userWithoutUsername)))
                .andDo(print())
                .andReturn();
        assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
    }
}
