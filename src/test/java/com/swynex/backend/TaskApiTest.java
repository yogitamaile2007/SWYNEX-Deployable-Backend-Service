package com.swynex.backend;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:mem:${random.uuid}", "spring.datasource.username=sa", "spring.datasource.password=", "spring.datasource.driver-class-name=org.h2.Driver", "app.api-key="})
@AutoConfigureMockMvc
class TaskApiTest {

    @Autowired
    MockMvc mvc;

    @Test
    void healthIsUp() throws Exception {
        mvc.perform(get("/actuator/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void sampleDataIsLoaded() throws Exception {
        mvc.perform(get("/api/tasks")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(8)));
    }

    @Test
    void fullCrudFlow() throws Exception {
        String body = mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New task\",\"priority\":\"HIGH\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New task"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(body, "$.id");

        mvc.perform(get("/api/tasks/" + id)).andExpect(status().isOk());

        mvc.perform(put("/api/tasks/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"DONE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.title").value("New task"));

        mvc.perform(delete("/api/tasks/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/tasks/" + id)).andExpect(status().isNotFound());
    }

    @Test
    void validationErrorsReturn400() throws Exception {
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/tasks").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"x\",\"status\":\"BAD\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void filtersAndStats() throws Exception {
        mvc.perform(get("/api/tasks").param("status", "DONE")).andExpect(status().isOk())
                .andExpect(jsonPath("$[*].status").value(org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.is("DONE"))));
        mvc.perform(get("/api/tasks").param("status", "nope")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/stats")).andExpect(status().isOk())
                .andExpect(jsonPath("$.total", greaterThanOrEqualTo(8)));
    }
}
