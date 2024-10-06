package com.example.tests.integration;

import com.example.Ter24Application;
import com.example.repositories.TeacherRepository;
import com.example.repositories.TERManagerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = Ter24Application.class)
@AutoConfigureMockMvc
public class ProjectIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TERManagerRepository managerRepository;

    @BeforeEach
    public void setUp() {
        // Clear out the repositories to prevent test data conflicts.
        teacherRepository.deleteAll();
        managerRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "Chef", roles = "MANAGER")
    void addTeacherPostNonExistingTeacher() throws Exception {
        // Ensure the POST request and check the response.
        mvc.perform(post("/teachers").contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": \"1\", \"name\": \"New Teacher\" }"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Teacher added successfully")));
    }

    @Test
    @WithMockUser(username = "lechef", roles = "MANAGER")
    void savingTeachersIsPossibleForManager() {
        // Logic to test saving the teacher
    }

    @Test
    @WithMockUser(username = "professeur", roles = "TEACHER")
    void testTeacherCreationInvalidRole() {
        // Logic where teacher tries to create another teacher
    }

    @Test
    public void testToStringHeureValide() {
        // Validate `toString` implementation
    }

    @Test
    public void testCreationHeureInvalideDepasseHeureMax() {
        // Test invalid hour creation, exceeding max limit
    }

    @Test
    public void testCreationHeureInvalideGranulariteFausse() {
        // Test invalid hour creation due to bad granularity
    }

    @Test
    @WithMockUser(username = "Chef", roles = "MANAGER")
    void addTeacherGet() throws Exception {
        // Ensure the GET request and check the response.
        mvc.perform(get("/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("New Teacher"));
    }
}